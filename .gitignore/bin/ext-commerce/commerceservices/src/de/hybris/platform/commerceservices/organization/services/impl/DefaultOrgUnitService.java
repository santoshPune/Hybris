/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 *
 */
package de.hybris.platform.commerceservices.organization.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.commerceservices.organization.daos.OrgUnitDao;
import de.hybris.platform.commerceservices.organization.services.OrgUnitMemberParameter;
import de.hybris.platform.commerceservices.organization.services.OrgUnitParameter;
import de.hybris.platform.commerceservices.organization.services.OrgUnitService;
import de.hybris.platform.commerceservices.organization.strategies.OrgUnitActivationStrategy;
import de.hybris.platform.commerceservices.organization.strategies.OrgUnitAuthorizationStrategy;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link OrgUnitService} interface.
 */
public class DefaultOrgUnitService implements OrgUnitService
{
	private static final Logger LOG = Logger.getLogger(DefaultOrgUnitService.class);

	private static final String PARAM_UNIT = "orgUnit";
	private static final String PARAM_UNIT_ACTIVE = "active";
	private static final String PARAM_UNIT_MEMBERS = "members";
	private static final String PARAM_UNIT_NAME = "name";
	private static final String PARAM_UNIT_PAGEABLE_DATA = "pageableData";
	private static final String PARAM_UNIT_TYPE = "type";
	private static final String PARAM_UNIT_UID = "uid";

	private ModelService modelService;
	private UserService userService;
	private OrgUnitActivationStrategy orgUnitActivationStrategy;
	private OrgUnitDao orgUnitDao;
	private OrgUnitAuthorizationStrategy orgUnitAuthorizationStrategy;

	@Override
	public void createUnit(final OrgUnitParameter parameter)
	{
		validateParameterNotNullStandardMessage(PARAM_UNIT_UID, parameter.getUid());
		validateParameterNotNullStandardMessage(PARAM_UNIT_NAME, parameter.getName());
		validateParameterNotNullStandardMessage(PARAM_UNIT_ACTIVE, parameter.getActive());
		getOrgUnitAuthorizationStrategy().validateCreatePermission(getUserService().getCurrentUser());

		final OrgUnitModel orgUnit = getModelService().create(OrgUnitModel.class);

		// essential attributes
		orgUnit.setUid(parameter.getUid());
		orgUnit.setName(parameter.getName());
		orgUnit.setActive(parameter.getActive());

		if (parameter.getParentUnit() != null)
		{
			setParentUnit(orgUnit, parameter.getParentUnit());
		}

		// optional attributes
		orgUnit.setDescription(parameter.getDescription());
		orgUnit.setLineOfBuisness(parameter.getLineOfBusiness());

		// additional attributes
		orgUnit.setSupplier(parameter.getSupplier() == null ? Boolean.FALSE : parameter.getSupplier());
		orgUnit.setBuyer(parameter.getBuyer() == null ? Boolean.FALSE : parameter.getBuyer());
		orgUnit.setManufacturer(parameter.getManufacturer() == null ? Boolean.FALSE : parameter.getManufacturer());
		orgUnit.setCarrier(parameter.getCarrier() == null ? Boolean.FALSE : parameter.getCarrier());

		modelService.save(orgUnit);
	}

	@Override
	public void updateUnit(final OrgUnitParameter parameter)
	{
		validateParameterNotNullStandardMessage(PARAM_UNIT, parameter.getOrgUnit());
		getOrgUnitAuthorizationStrategy().validateEditPermission(getUserService().getCurrentUser());

		final OrgUnitModel orgUnit = parameter.getOrgUnit();

		setParentUnit(orgUnit, parameter.getParentUnit());

		modelService.save(orgUnit);
	}

	@Override
	public Optional<OrgUnitModel> getUnitForUid(final String uid)
	{
		validateParameterNotNullStandardMessage(PARAM_UNIT_UID, uid);
		getOrgUnitAuthorizationStrategy().validateViewPermission(getUserService().getCurrentUser());

		try
		{
			return Optional.ofNullable(getUserService().getUserGroupForUID(uid, OrgUnitModel.class));
		}
		catch (UnknownIdentifierException | ClassMismatchException e)
		{
			LOG.error(e);
		}

		return Optional.empty();
	}

	@Override
	public void activateUnit(final OrgUnitModel orgUnit)
	{
		validateParameterNotNullStandardMessage(PARAM_UNIT, orgUnit);
		getOrgUnitAuthorizationStrategy().validateEditPermission(getUserService().getCurrentUser());

		getOrgUnitActivationStrategy().activateUnit(orgUnit);
	}

	@Override
	public void deactivateUnit(final OrgUnitModel orgUnit)
	{
		validateParameterNotNullStandardMessage(PARAM_UNIT, orgUnit);
		getOrgUnitAuthorizationStrategy().validateEditPermission(getUserService().getCurrentUser());

		getOrgUnitActivationStrategy().deactivateUnit(orgUnit);
	}

	@Override
	public void addMembers(final OrgUnitMemberParameter parameter)
	{
		addRemoveMembers(parameter, (memberGroups, orgUnit) -> memberGroups.add(orgUnit));
	}

	@Override
	public void removeMembers(final OrgUnitMemberParameter parameter)
	{
		addRemoveMembers(parameter, (memberGroups, orgUnit) -> memberGroups.remove(orgUnit));
	}

	@Override
	public <T extends PrincipalModel> SearchPageData<T> getMembers(final OrgUnitMemberParameter<T> parameter)
	{
		validateParameterNotNullStandardMessage(PARAM_UNIT_UID, parameter.getUid());
		validateParameterNotNullStandardMessage(PARAM_UNIT_TYPE, parameter.getType());
		validateParameterNotNullStandardMessage(PARAM_UNIT_PAGEABLE_DATA, parameter.getPageableData());
		getOrgUnitAuthorizationStrategy().validateViewPermission(getUserService().getCurrentUser());

		final Optional<OrgUnitModel> unitOptional = getUnitForUid(parameter.getUid());
		if (unitOptional.isPresent())
		{
			return getOrgUnitDao().findMembersOfType(unitOptional.get(), parameter.getType(), parameter.getPageableData());
		}
		LOG.warn("Unit with uid [" + parameter.getUid() + "] does not exist.");
		return createEmptySearchPageData();
	}

	@Override
	public Optional<OrgUnitModel> getParent(final OrgUnitModel orgUnit)
	{
		validateParameterNotNullStandardMessage(PARAM_UNIT, orgUnit);
		getOrgUnitAuthorizationStrategy().validateViewPermission(getUserService().getCurrentUser());

		return Optional.ofNullable(
				(OrgUnitModel) CollectionUtils.find(orgUnit.getGroups(), PredicateUtils.instanceofPredicate(OrgUnitModel.class)));
	}

	protected <T> SearchPageData<T> createEmptySearchPageData()
	{
		final SearchPageData<T> searchPageData = new SearchPageData<>();
		final PaginationData pagination = new PaginationData();
		pagination.setTotalNumberOfResults(0);
		searchPageData.setPagination(pagination);
		searchPageData.setResults(Collections.emptyList());
		searchPageData.setSorts(Collections.emptyList());
		return searchPageData;
	}

	protected <T extends PrincipalModel> void addRemoveMembers(final OrgUnitMemberParameter<T> parameter,
			final BiConsumer<Set<PrincipalGroupModel>, OrgUnitModel> consumer)
	{
		validateParameterNotNullStandardMessage(PARAM_UNIT_UID, parameter.getUid());
		validateParameterNotNullStandardMessage(PARAM_UNIT_MEMBERS, parameter.getMembers());
		getOrgUnitAuthorizationStrategy().validateEditPermission(getUserService().getCurrentUser());

		final Optional<OrgUnitModel> unitOptional = getUnitForUid(parameter.getUid());

		if (unitOptional.isPresent())
		{
			for (final PrincipalModel member : parameter.getMembers())
			{
				final Set<PrincipalGroupModel> memberGroups = new HashSet<>(member.getGroups());
				consumer.accept(memberGroups, unitOptional.get());
				member.setGroups(memberGroups);
				getModelService().save(member);
			}
		}
		else
		{
			LOG.warn("Unit with uid [" + parameter.getUid() + "] does not exist.");
		}
	}

	protected void setParentUnit(final OrgUnitModel orgUnit, final OrgUnitModel newParentUnit)
	{
		validateParameterNotNullStandardMessage(PARAM_UNIT, orgUnit);

		final OrgUnitModel currentParentUnit = getParent(orgUnit).orElse(null);

		if (currentParentUnit == null && newParentUnit == null)
		{
			return;
		}
		if (currentParentUnit != null && currentParentUnit.equals(newParentUnit))
		{
			return;
		}

		getOrgUnitAuthorizationStrategy().validateEditParentPermission(getUserService().getCurrentUser());

		final Set<PrincipalGroupModel> groups = orgUnit.getGroups() == null ? new HashSet<>() : new HashSet<>(orgUnit.getGroups());

		if (currentParentUnit != null)
		{
			groups.remove(currentParentUnit);
		}
		if (newParentUnit != null)
		{
			groups.add(newParentUnit);
		}

		orgUnit.setGroups(groups);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected OrgUnitActivationStrategy getOrgUnitActivationStrategy()
	{
		return orgUnitActivationStrategy;
	}

	@Required
	public void setOrgUnitActivationStrategy(final OrgUnitActivationStrategy orgUnitActivationStrategy)
	{
		this.orgUnitActivationStrategy = orgUnitActivationStrategy;
	}

	protected OrgUnitDao getOrgUnitDao()
	{
		return orgUnitDao;
	}

	@Required
	public void setOrgUnitDao(final OrgUnitDao orgUnitDao)
	{
		this.orgUnitDao = orgUnitDao;
	}

	protected OrgUnitAuthorizationStrategy getOrgUnitAuthorizationStrategy()
	{
		return orgUnitAuthorizationStrategy;
	}

	@Required
	public void setOrgUnitAuthorizationStrategy(final OrgUnitAuthorizationStrategy orgUnitAuthorizationStrategy)
	{
		this.orgUnitAuthorizationStrategy = orgUnitAuthorizationStrategy;
	}
}
