package services;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import domain.entities.Product;
import exceptions.NegativeValueException;

public class CategoryServiceTest {
	
	ProductService productService;
	CategoryService categoryService;
	
	@Before
	public void setup() {
		productService = new ProductService();
		categoryService = new CategoryService();
	}

	@Test
	public void mustInstantiateCategory() {
		// cenário
		Map<String, String> productStr = new HashMap<>();
		productStr.put("name", "Câmera Fotográfica");
		productStr.put("price", Double.toString(555.55));
		productStr.put("quantity", Integer.toString(5));
		productStr.put("category", "Eletro");
		

		// ação
		Product newProduct = null;
		try {
			newProduct = productService.createProduct(productStr);
			newProduct.setCategory(categoryService.createCategory(productStr));
		} catch (NegativeValueException e) {
			System.out.println(e.getMessage());
		}

		// verificação
		Assert.assertEquals(productStr.get("category"), newProduct.getCategory().getName());
	}

}