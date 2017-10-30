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
 */
package de.hybris.platform.configurablebundleservices.constraints;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.configurablebundleservices.model.AbstractBundleRuleModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@IntegrationTest
public class PriceRuleValidationTest extends AbstractBundleValidationTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    @Ignore("Corresponding validator is missing")
    public void shouldDenyNonLeafsToHavePriceRules() throws ImpExException
    {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(endsWith("Only leaf bundle templates can have price rules.\n"));
        // We have BundleTemplatePriceRulesAndChildrenValidator that checks this case, but it
        // triggers only when a BundleTemplateModel is saved.
        // So, we need an extra validator for bundle rules that does the same check.
        importString(
                "INSERT_UPDATE ChangeProductPriceBundleRule;id[unique=true,forceWrite=true,allownull=true];bundleTemplate(id, version, $catalogversion)[unique=true];conditionalProducts(code, $catalogversion);targetProducts(code, $catalogversion);price;$catalogversion;ruleType(code)[default=ANY];currency(isocode)[default=USD, unique=true]\n"
                + ";price_PRODUCT05_in_NestedComponent1;NestedComponent1:1.0;PREMIUM01,PREMIUM02;PRODUCT05;500 USD\n"
        );
    }

    @Test
    public void shouldInformAboutEmptyConditionalProducts() throws ImpExException
    {
        importString(
                "INSERT_UPDATE ChangeProductPriceBundleRule;id[unique=true,forceWrite=true,allownull=true];bundleTemplate(id, version, $catalogversion)[unique=true];targetProducts(code, $catalogversion);price;$catalogversion;ruleType(code)[default=ANY];currency(isocode)[default=USD, unique=true]\n"
                + ";price_PRODUCT01_in_ProductComponent1;ProductComponent1:1.0;PRODUCT02;200 USD\n"
        );
        Set<HybrisConstraintViolation> violations = validate("price_PRODUCT01_in_ProductComponent1");
        assertThat(violations, hasItem(allOf(
                hasProperty(FIELD_MESSAGE, is("The price rule does not contain any conditional products. This means the price changes will be applied every time.")),
                hasProperty(FIELD_SEVERITY, is(Severity.INFO))
        )));
    }

    @Test
    public void shouldWarnAboutEmptyTargetProductList() throws ImpExException
    {
        importString(
                "INSERT ChangeProductPriceBundleRule;id[unique=true,forceWrite=true,allownull=true];bundleTemplate(id, version, $catalogversion)[unique=true];conditionalProducts(code, $catalogversion);targetProducts(code, $catalogversion);price;$catalogversion;ruleType(code)[default=ANY];currency(isocode)[default=USD, unique=true]\n"
                + ";price_NoTarget_ProductComponent1;ProductComponent1:1.0;PRODUCT01,PRODUCT02;;500 USD\n"
        );
        Set<HybrisConstraintViolation> violations = validate("price_NoTarget_ProductComponent1");
        assertThat(violations, hasItem(allOf(
                hasProperty(FIELD_MESSAGE, is("Each priceRule must at least have one target product assigned. Please add one target product or remove this price rule.")),
                hasProperty(FIELD_SEVERITY, is(Severity.WARN))
        )));
    }

    @Test
    public void shouldWarnAboutForeignersInTargetProducts() throws ImpExException
    {
        importString(
                "INSERT ChangeProductPriceBundleRule;id[unique=true];bundleTemplate(id, version, $catalogversion)[unique=true];conditionalProducts(code, $catalogversion);targetProducts(code, $catalogversion);price;$catalogversion;ruleType(code)[default=ANY];currency(isocode)[default=USD, unique=true]\n"
                + ";price_Foreign_ProductComponent1;ProductComponent1:1.0;PRODUCT01,PRODUCT02;OTHER01,OTHER02;500 USD\n"
        );
        Set<HybrisConstraintViolation> violations = validate("price_Foreign_ProductComponent1");
        assertThat(violations, containsInAnyOrder(
                hasProperty(FIELD_MESSAGE,
                        is("This price rule has a target product 'OTHER01' which is not part of the bundle template. Please either add the product 'OTHER01' to the bundle template list or remove this price rule.")),
                hasProperty(FIELD_MESSAGE,
                        is("This price rule has a target product 'OTHER02' which is not part of the bundle template. Please either add the product 'OTHER02' to the bundle template list or remove this price rule."))
                ));
    }

    @Test
    public void shouldWarnAboutStandaloneInTargetProducts() throws ImpExException
    {
        importString(
                "INSERT ChangeProductPriceBundleRule;id[unique=true];bundleTemplate(id, version, $catalogversion)[unique=true];conditionalProducts(code, $catalogversion);targetProducts(code, $catalogversion);price;$catalogversion;ruleType(code)[default=ANY];currency(isocode)[default=USD, unique=true]\n"
                + ";price_Standalone_ProductComponent1;ProductComponent1:1.0;PRODUCT01,PRODUCT02;STANDALONE01;500 USD\n"
        );
        Set<HybrisConstraintViolation> violations = validate("price_Standalone_ProductComponent1");
        assertThat(violations, hasItem(hasProperty("localizedMessage",
                is("This price rule has a target product 'STANDALONE01' which is not part of the bundle template. Please either add the product 'STANDALONE01' to the bundle template list or remove this price rule."))));
    }

    @Test
    public void shouldWarnAboutMixedInTargetProducts() throws ImpExException
    {
        importString(
                "INSERT ChangeProductPriceBundleRule;id[unique=true];bundleTemplate(id, version, $catalogversion)[unique=true];conditionalProducts(code, $catalogversion);targetProducts(code, $catalogversion);price;$catalogversion;ruleType(code)[default=ANY];currency(isocode)[default=USD, unique=true]\n"
                + ";price_Mixed_ProductComponent1;ProductComponent1:1.0;PRODUCT01,PRODUCT02;STANDALONE01,OTHER01;500 USD\n"
        );
        Set<HybrisConstraintViolation> violations = validate("price_Mixed_ProductComponent1");
        assertThat(violations, containsInAnyOrder(
                hasProperty(FIELD_MESSAGE,
                        is("This price rule has a target product 'STANDALONE01' which is not part of the bundle template. Please either add the product 'STANDALONE01' to the bundle template list or remove this price rule.")),
                hasProperty(FIELD_MESSAGE,
                        is("This price rule has a target product 'OTHER01' which is not part of the bundle template. Please either add the product 'OTHER01' to the bundle template list or remove this price rule."))
        ));
    }

    @Test
    public void shouldRejectRulesWithForeignTargetProducts() throws ImpExException
    {
        importString(
                "INSERT_UPDATE ChangeProductPriceBundleRule;id[unique=true,forceWrite=true,allownull=true];bundleTemplate(id, version, $catalogversion)[unique=true];conditionalProducts(code, $catalogversion);targetProducts(code, $catalogversion);price;$catalogversion;ruleType(code)[default=ANY];currency(isocode)[default=USD, unique=true]\n"
                + ";price_PREMIUM05_in_ProductComponent1;ProductComponent1:1.0;PRODUCT01,PRODUCT02;PREMIUM03;500 USD");
        Set<HybrisConstraintViolation> violations = validate("price_PREMIUM05_in_ProductComponent1");
        assertThat(violations, hasItem(
                hasProperty(FIELD_MESSAGE,
                        is("This price rule has a target product 'PREMIUM03' which is not part of the bundle template. Please either add the product 'PREMIUM03' to the bundle template list or remove this price rule."))
        ));
    }

    protected Set<HybrisConstraintViolation> validate(final String ruleId)
    {
        return getValidationService().validate(
                getBundleRule(ruleId),
                Collections.singletonList(getValidationService().getDefaultConstraintGroup())
        );
    }

    protected AbstractBundleRuleModel getBundleRule(final String ruleId)
    {
        final AbstractBundleRuleModel rule = new AbstractBundleRuleModel();
        rule.setId(ruleId);
        rule.setCatalogVersion(getCatalog());
        return flexibleSearchService.getModelByExample(rule);
    }
}
