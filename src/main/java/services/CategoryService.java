package services;

import java.util.Map;

import domain.entities.Category;

public class CategoryService {
	
	public Category createCategory(Map<String, String> productStr) {
		return new Category(productStr.get("category"));
	}

}
