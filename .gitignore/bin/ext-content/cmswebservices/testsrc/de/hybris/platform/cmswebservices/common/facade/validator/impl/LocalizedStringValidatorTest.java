package de.hybris.platform.cmswebservices.common.facade.validator.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.AbstractCMSComponentData;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LocalizedStringValidatorTest
{
	private static final String ENGLISH = Locale.ENGLISH.toString();
	private static final String NAME = "name";
	private static final String NAME_VALUE = "test-name-value";

	private final LocalizedStringValidator validator = new LocalizedStringValidator();

	private AbstractCMSComponentData data;
	private Errors errors;

	@Before
	public void setUp()
	{
		data = new AbstractCMSComponentData();
		errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
	}

	@Test
	public void shouldValidateString()
	{
		validator.validate(ENGLISH, NAME, NAME_VALUE, errors);

		assertThat(errors.getFieldErrorCount(), is(0));
	}

	@Test
	public void shouldValidateStringNull()
	{
		validator.validate(ENGLISH, NAME, null, errors);

		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldError(NAME).getCode(), is(CmswebservicesConstants.FIELD_REQUIRED_L10N));
		assertThat(errors.getFieldError(NAME).getArguments()[0], is(ENGLISH));
	}

}
