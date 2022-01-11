package domain.entities;

import java.util.Objects;

public class Product {
	
	private String name;
	private Double price;
	private Integer quantity;
	
	private Category category;

	public Product(String name, Double price, Integer quantity) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void increaseQuantity(int amount) {
		quantity += amount;
	}
	
	public void decreaseQuantity(int amount) {
		quantity -= amount;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(category, name, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return Objects.equals(category, other.category) && Objects.equals(name, other.name)
				&& Objects.equals(price, other.price);
	}

	@Override
	public String toString() {
		return "Produto: " + name + ", "
				+ "preço: R$ " + String.format("%.2f", price) + ", "
				+ "quantidade: " + quantity + ", "
				+ "categoria: " + category.getName() + ".";
	}

}
