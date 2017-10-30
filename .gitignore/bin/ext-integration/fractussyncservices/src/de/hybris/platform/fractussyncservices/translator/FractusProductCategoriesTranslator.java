package de.hybris.platform.fractussyncservices.translator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;


public class FractusProductCategoriesTranslator extends AbstractSpecialValueTranslator
{
	private ModelService modelService;
	private final String COMMA = ",";

	@Override
	public void init(SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
	{
		modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
	}

	@Override
	public String performExport(final Item item) throws ImpExException
	{
		if (item instanceof Product)
		{
			ProductModel productModel = getModelService().get(item);

			return StringUtils
					.join(productModel.getSupercategories().stream().map(CategoryModel::getCode).collect(Collectors.toList()), COMMA);
		}

		return StringUtils.EMPTY;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
