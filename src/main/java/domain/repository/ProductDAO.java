package domain.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import domain.entities.Product;
import exceptions.NegativeValueException;
import services.CategoryService;

public class ProductDAO implements DAO<Product> {

	private Repository repo = new Repository();
	private CategoryService categoryService = new CategoryService();

	@Override
	public List<Product> getAll() {
		List<Map<String, String>> productsStr = repo.selectAllProducts();
		List<Product> products = new ArrayList<>();
		for (Map<String, String> prodStr : productsStr) {
			try {
				Product newProduct = create(prodStr);
				newProduct.setCategory(categoryService.createCategory(prodStr));
				products.add(newProduct);
			} catch (NegativeValueException e) {
				System.out.println("Produto da base de dados não carregado: " + e.getMessage());
			}
		}
		return products;
	}

	@Override
	public void add(Product product) {
		List<Product> products = getAll();
		Product oldProduct = products.stream().filter(p -> p.equals(product)).findFirst().orElse(null);
		if (oldProduct != null) {
			int position = products.indexOf(oldProduct);
			oldProduct.increaseQuantity(product.getQuantity());
			products.set(position, oldProduct);
		} else {
			products.add(product);
		}
		products.sort(compareProducts);
		repo.writeDBFile(productToSave(products));
	}

	@Override
	public Product create(Map<String, String> pStr) throws NegativeValueException {
		if (Double.parseDouble(pStr.get("price")) < 0.0) {
			throw new NegativeValueException("O preço do produto não pode ser negativo.");
		}
		if (Integer.parseInt(pStr.get("quantity")) < 0) {
			throw new NegativeValueException("A quantidade do produto não pode ser negativa.");
		}
		return new Product(pStr.get("name"), Double.parseDouble(pStr.get("price")),
				Integer.parseInt(pStr.get("quantity")));
	}

	@Override
	public void update(int position, Product updatedProduct) {
		List<Product> products = getAll();
		products.set(position, updatedProduct);
		products.sort(compareProducts);
		repo.writeDBFile(productToSave(products));
	}

	@Override
	public void delete(int position) {
		List<Product> products = getAll();
		products.remove(position);
		repo.writeDBFile(productToSave(products));
	}

	private List<String> productToSave(List<Product> products) {
		return products.stream().map(
				p -> "" + p.getName() + "," + p.getPrice() + "," + p.getQuantity() + "," + p.getCategory().getName())
				.collect(Collectors.toList());
	}

	Comparator<Product> compareProducts = (prod1, prod2) -> {
		if (prod1.getCategory().getName().equals(prod2.getCategory().getName())) {
			return prod1.getName().compareToIgnoreCase(prod2.getName());
		}
		return prod1.getCategory().getName().compareToIgnoreCase(prod2.getCategory().getName());
	};

}
