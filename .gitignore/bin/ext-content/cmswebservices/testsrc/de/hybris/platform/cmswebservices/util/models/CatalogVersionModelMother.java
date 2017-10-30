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
 */
package de.hybris.platform.cmswebservices.util.models;

import static de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother.CatalogVersion.ONLINE;
import static de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother.CatalogVersion.STAGED;
import static de.hybris.platform.cmswebservices.util.models.ContentCatalogModelMother.CatalogTemplate.ID_APPLE;
import static org.apache.commons.lang3.StringUtils.lowerCase;

import de.hybris.platform.catalog.daos.CatalogVersionDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cmswebservices.util.builder.CatalogVersionModelBuilder;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Required;


public class CatalogVersionModelMother extends AbstractModelMother<CatalogVersionModel>
{
	public enum CatalogVersion {
		ONLINE,
		STAGED;

		public String getVersion() {
			return lowerCase(this.name());
		}
	}

	private CatalogVersionDao catalogVersionDao;

	private ContentCatalogModelMother catalogModelMother;
	private CurrencyModelMother currencyModelMother;
	private ContentPageModelMother contentPageModelMother;
	private CronJobService cronJobService;
	private PermissionManagementService permissionManagementService;
	private UserService userService;

	protected CatalogVersionModel defaultCatalogVersion(final boolean active)
	{
		return CatalogVersionModelBuilder.aModel().withActive(active)
				.withDefaultCurrency(currencyModelMother.createUSDollar()).build();
	}

	public CatalogVersionModel createAppleStagedCatalogVersionModel()
	{
		return createCatalogVersionModelByIdAndVersion(ID_APPLE.name(), STAGED);
	}

	public CatalogVersionModel createStagedCatalogVersionModelWithId(final String catalogId)
	{
		return createCatalogVersionModelByIdAndVersion(catalogId, STAGED);
	}

	public CatalogVersionModel createOnlineCatalogVersionModelWithId(final String catalogId)
	{
		return createCatalogVersionModelByIdAndVersion(catalogId, ONLINE);
	}

	private CatalogVersionModel createCatalogVersionModelByIdAndVersion(final String catalogId, final CatalogVersion version)
	{
		return getFromCollectionOrSaveAndReturn(() -> getCatalogVersionDao().findCatalogVersions(catalogId, version.getVersion()),
				() -> CatalogVersionModelBuilder
				.fromModel(defaultCatalogVersion(true))
				.withCatalog(getCatalogModelMother().createContentCatalogModelWithIdAndName(catalogId, ID_APPLE.getFirstInstanceOfHumanName()))
				.withVersion(version.getVersion()).build());
	}

	public CatalogVersionModel createAppleOnlineCatalogVersionModel()
	{
		return createCatalogVersionModelByIdAndVersion(ID_APPLE.name(), ONLINE);
	}

	public void performCatalogSyncronization(final CatalogVersionModel source, final CatalogVersionModel target)
	{
		final SyncItemJobModel syncItemJobModel = new SyncItemJobModel();
		syncItemJobModel.setActive(true);
		syncItemJobModel.setSourceVersion(source);
		syncItemJobModel.setTargetVersion(target);

		final SyncItemCronJobModel cronJob = new SyncItemCronJobModel();
		cronJob.setCode(UUID.randomUUID().toString());
		cronJob.setJob(syncItemJobModel);
		getModelService().saveAll(cronJob, syncItemJobModel);
		final boolean synchronousJob = true;
		getCronJobService().performCronJob(cronJob, synchronousJob);
	}

	public SyncItemCronJobModel createCatalogSyncronizationSyncItemCronJobModel(final CatalogVersionModel source, final CatalogVersionModel target)
	{
		final SyncItemJobModel syncItemJobModel = new SyncItemJobModel();
		syncItemJobModel.setActive(true);
		syncItemJobModel.setSourceVersion(source);
		syncItemJobModel.setTargetVersion(target);

		final SyncItemCronJobModel cronJob = new SyncItemCronJobModel();
		cronJob.setCode(UUID.randomUUID().toString());
		cronJob.setJob(syncItemJobModel);
		getModelService().saveAll(cronJob, syncItemJobModel);
		getCronJobService().performCronJob(cronJob);
		return cronJob;
	}

	public CatalogVersionModel createAppleCatalogVersionModel(final String version)
	{
		final CatalogVersionModel catalogVersion = CatalogVersionModelBuilder.fromModel(defaultCatalogVersion(false))
				.withCatalog(catalogModelMother.createAppleContentCatalogModel())
				.withVersion(version)
				.build();

		final CatalogVersionModel catalogVersionSaved = getFromCollectionOrSaveAndReturn(
				() -> getCatalogVersionDao().findCatalogVersions(ID_APPLE.name(), version), () -> catalogVersion);

		return catalogVersionSaved;
	}

	public CatalogVersionModel createCatalogVersionModel(final String catalogId, final String version)
	{
		final CatalogVersionModel catalogVersion = CatalogVersionModelBuilder.fromModel(defaultCatalogVersion(false))
				.withCatalog(catalogModelMother.createContentCatalogModelWithIdAndName(catalogId, catalogId))
				.withVersion(version)
				.build();

		final CatalogVersionModel catalogVersionSaved = getFromCollectionOrSaveAndReturn(
				() -> getCatalogVersionDao().findCatalogVersions(catalogId, version), () -> catalogVersion);

		return catalogVersionSaved;
	}

	public ContentCatalogModelMother getCatalogModelMother()
	{
		return catalogModelMother;
	}

	@Required
	public void setCatalogModelMother(final ContentCatalogModelMother catalogModelMother)
	{
		this.catalogModelMother = catalogModelMother;
	}

	public CatalogVersionDao getCatalogVersionDao()
	{
		return catalogVersionDao;
	}

	@Required
	public void setCatalogVersionDao(final CatalogVersionDao catalogVersionDao)
	{
		this.catalogVersionDao = catalogVersionDao;
	}

	public CurrencyModelMother getCurrencyModelMother()
	{
		return currencyModelMother;
	}

	@Required
	public void setCurrencyModelMother(final CurrencyModelMother currencyModelMother)
	{
		this.currencyModelMother = currencyModelMother;
	}

	public ContentPageModelMother getContentPageModelMother() {
		return contentPageModelMother;
	}

	@Required
	public void setContentPageModelMother(final ContentPageModelMother contentPageModelMother) {
		this.contentPageModelMother = contentPageModelMother;
	}
	@Required
	protected CronJobService getCronJobService()
	{
		return cronJobService;
	}

	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}

	public PermissionManagementService getPermissionManagementService()
	{
		return permissionManagementService;
	}

	@Required
	public void setPermissionManagementService(final PermissionManagementService permissionManagementService)
	{
		this.permissionManagementService = permissionManagementService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}
