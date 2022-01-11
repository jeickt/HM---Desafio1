package services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import domain.entities.Product;
import exceptions.NegativeValueException;

public class CenterService {
	
	static ProductService productservice = new ProductService();
	static CategoryService categoryservice = new CategoryService();
	
	public void performAddProduct(Map<String, String> productStr) throws NegativeValueException {
		Product newProduct = productservice.createProduct(productStr);
		newProduct.setCategory(categoryservice.createCategory(productStr));
		productservice.addProduct(newProduct);
	}
	
	public void performUpdateProduct(int position, Map<String, String> updatedProductStr) throws NegativeValueException {
		Product updatedProduct = productservice.createProduct(updatedProductStr);
		updatedProduct.setCategory(categoryservice.createCategory(updatedProductStr));
		productservice.updateOneProduct(position, updatedProduct);
	}
	
	public void performDeleteProduct(int position) {
		productservice.deleteOneProduct(position);
	}
	
	public List<Product> performGetAllProducts() {
		return productservice.getAllProducts();
	}
	
	public List<Product> performLoadNewProducts(String path) throws FileNotFoundException, IOException {
		return productservice.loadNewProducts(path);
	}
	
	public void performImportProduct(List<Product> newProducts) {
		productservice.importProducts(newProducts);
	}

}
