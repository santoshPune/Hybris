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
package de.hybris.platform.smarteditwebservices.validation.facade.impl;


import de.hybris.platform.smarteditwebservices.validation.factory.ErrorFactory;
import de.hybris.platform.smarteditwebservices.validation.facade.FacadeValidationService;
import de.hybris.platform.smarteditwebservices.validation.facade.ValidationException;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Default implementation of a facade level validation facade. This facade will create a new instance of the
 * {@link Errors}, ensure that the given validator can support the object to be validated, then perform the validation.
 *
 * <p>
 * If any validation errors were found, then a {@link ValidationException} is thrown.
 * </p>
 */
public class DefaultFacadeValidationService implements FacadeValidationService
{
	private ErrorFactory validatorErrorFactory;

	@Override
	public void validate(Validator validator, Object validatee) throws ValidationException
	{
		final Errors errors = getValidatorErrorFactory().createInstance(validatee);
		validateInternal(validator, validatee, errors);
	}

	@Override
	public void validate(Validator validator, Object validatee, Object bindingObject) throws ValidationException
	{
		final Errors errors = getValidatorErrorFactory().createInstance(bindingObject);
		validateInternal(validator, validatee, errors);
	}

	/**
	 * Validate the object using the provided binded errors
	 * 
	 * @param validator
	 * @param validatee
	 * @param errors
	 * @throws ValidationException
	 */
	protected void validateInternal(Validator validator, Object validatee, Errors errors) throws ValidationException
	{
		if(!validator.supports(validatee.getClass()))
		{
			throw new IllegalArgumentException("Validator \"" + validator.getClass().getSimpleName() + "\" does not support \""
					+ validatee.getClass().getSimpleName() + "\".");
		}

		validator.validate(validatee, errors);
		if (errors.hasErrors())
		{
			throw new ValidationException(errors);
		}
	}

	protected ErrorFactory getValidatorErrorFactory()
	{
		return validatorErrorFactory;
	}

	@Required
	public void setValidatorErrorFactory(ErrorFactory validatorErrorFactory)
	{
		this.validatorErrorFactory = validatorErrorFactory;
	}

}
