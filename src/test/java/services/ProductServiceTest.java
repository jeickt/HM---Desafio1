package services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import domain.entities.Product;
import exceptions.NegativeValueException;

public class ProductServiceTest {

	ProductService productService;

	@Before
	public void setup() {
		productService = new ProductService();
	}

	@Test
	public void mustInstantiateProduct() throws Exception {
		// cenário
		Map<String, String> productStr = new HashMap<>();
		productStr.put("name", "Câmera Fotográfica");
		productStr.put("price", Double.toString(555.55));
		productStr.put("quantity", Integer.toString(5));
		productStr.put("category", "Eletro");

		// ação
		Product newProduct = productService.createProduct(productStr);

		// verificação
		Assert.assertEquals(productStr.get("name"), newProduct.getName());
		Assert.assertEquals(Double.parseDouble(productStr.get("price")), newProduct.getPrice(), 0.00);
		Assert.assertEquals(Integer.parseInt(productStr.get("quantity")), (int) newProduct.getQuantity());
	}

	@Test(expected = NegativeValueException.class) // para tratar de exceção por preço negativo
	public void mustThrowPriceError() throws Exception {
		// cenário
		Map<String, String> productStr = new HashMap<>();
		productStr.put("name", "Câmera Fotográfica");
		productStr.put("price", Double.toString(-555.55));
		productStr.put("quantity", Integer.toString(5));
		productStr.put("category", "Eletro");

		// ação
		productService.createProduct(productStr);
	}

	@Test(expected = NegativeValueException.class) // para tratar de exceção por quantidade negativa
	public void mustThrowQuantityError() throws Exception {
		// cenário
		Map<String, String> productStr = new HashMap<>();
		productStr.put("name", "Câmera Fotográfica");
		productStr.put("price", Double.toString(555.55));
		productStr.put("quantity", Integer.toString(-5));
		productStr.put("category", "Eletro");

		// ação
		productService.createProduct(productStr);
	}

	@Test
	public void mustLoadProducts() {
		// cenário
		Locale.setDefault(Locale.US);
		String path = "." + File.separator + "mostruario_fabrica.csv";

		// ação
		List<Product> products = null;
		try {
			products = productService.loadNewProducts(path);
		} catch (IOException e) {
			System.out.println("Falha ao carregar o arquivo 'mostruario_fabrica.csv' na pasta de origem do programa.");
		}

		// verificação
		Assert.assertEquals(58.71d, products.get(3).getPrice(), 0.00);
		Assert.assertEquals("MEIO DE TRANSPORTE", products.get(6).getCategory().getName());
	}

}