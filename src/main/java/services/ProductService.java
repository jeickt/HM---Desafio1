package services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.entities.Product;
import domain.repository.ProductDAO;
import exceptions.NegativeValueException;

public class ProductService {

	static CategoryService categoryservice = new CategoryService();
	static ProductDAO productDAO = new ProductDAO();

	List<Product> getAllProducts() {
		return productDAO.getAll();
	}

	void addProduct(Product newProduct) throws NegativeValueException {
		if (newProduct.getPrice() < 0.0) {
			throw new NegativeValueException("O preço do produto não pode ser negativo.");
		}
		if (newProduct.getQuantity() < 0) {
			throw new NegativeValueException("A quantidade do produto não pode ser negativa.");
		}
		productDAO.add(newProduct);
	}

	Product createProduct(Map<String, String> productStr) throws NegativeValueException {
		return productDAO.create(productStr);
	}

	void updateOneProduct(int position, Product updatedProduct) throws NegativeValueException {
		if (updatedProduct.getPrice() < 0.0) {
			throw new NegativeValueException("O preço do produto não pode ser negativo.");
		}
		if (updatedProduct.getQuantity() < 0) {
			throw new NegativeValueException("A quantidade do produto não pode ser negativa.");
		}
		productDAO.update(position, updatedProduct);
	}

	void deleteOneProduct(int position) {
		productDAO.delete(position);
	}

	void importProducts(List<Product> products) {
		for (Product product : products) {
			productDAO.add(product);
		}
	}

	List<Product> loadNewProducts(String path) throws FileNotFoundException, IOException {
		List<Product> products = new ArrayList<Product>();

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = br.readLine();
			while (line != null) {
				if ("codigo".equals(line.split(",")[1].substring(0, 6))) {
					line = br.readLine();
					continue;
				}
				String[] content = getCleanArray(line);

				Map<String, String> productStr = new HashMap<>();
				productStr.put("name", content[0]);
				productStr.put("price", content[1]);
				productStr.put("quantity", content[2]);
				productStr.put("category", content[3]);

				Product newProduct;
				try {
					newProduct = productDAO.create(productStr);
					newProduct.setCategory(categoryservice.createCategory(productStr));
					products.add(newProduct);
				} catch (NegativeValueException e) {
					System.out.println("Erro na instanciação de produto: " + e.getMessage());
				}

				line = br.readLine();
			}
		}
		return products;
	}

	private String[] getCleanArray(String line) {
		String[] parts = line.split("\"");
		List<String> strings = new ArrayList<>();
		for (int i = 0; i < parts.length; i++) {
			if (i % 2 == 0) {
				String[] cels = parts[i].split(",");
				for (String cel : cels) {
					if (!cel.isEmpty()) {
						strings.add(cel);
					}
				}
			} else {
				strings.add(parts[i]);
			}
		}

		String[] content = new String[4];

		double basePrice = Double.parseDouble(strings.get(6).replace(',', '.'));
		double tax = Double.parseDouble(strings.get(7).replace(',', '.'));
		DecimalFormat decimal = new DecimalFormat("0.00");
		String finalPrice = decimal.format(basePrice * (tax / 100 + 1) * 1.45);

		content[0] = strings.get(3);
		content[1] = finalPrice;
		content[2] = Integer.toString(1);
		content[3] = strings.get(5);
		return content;
	}

}
