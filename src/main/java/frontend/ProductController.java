package frontend;

import static frontend.UserInteraction.conclude;
import static frontend.UserInteraction.readDoubleFromUser;
import static frontend.UserInteraction.readIntFromUser;
import static frontend.UserInteraction.readNextLineFromUser;
import static frontend.UserInteraction.showEditOptions;

import java.util.HashMap;
import java.util.Map;

import domain.entities.Product;
import exceptions.NegativeValueException;

public class ProductController {
	
	public static Map<String, String> setNewProductString() {
		Map<String, String> productStr = new HashMap<>();
		productStr.put("name", readNextLineFromUser("Informe o nome do produto: "));
		productStr.put("price", Double.toString(getPrice("Informe o seu preço: ")));
		productStr.put("quantity", Integer.toString(getQuantity("Informe sua quantidade em estoque: ")));
		productStr.put("category", readNextLineFromUser("Informe a categoria do produto: "));

		return productStr;
	}
	
	public static Map<String, String> setAtributesString(Product product) {
		Map<String, String> updatedProductStr = new HashMap<>();
		updatedProductStr.put("name", product.getName());
		updatedProductStr.put("price", Double.toString(product.getPrice()));
		updatedProductStr.put("quantity", Integer.toString(product.getQuantity()));
		updatedProductStr.put("category", product.getCategory().getName());
		
		int option = showEditOptions();
		while (option != 6) {
			switch(option) {
			case 1: updatedProductStr = updateName(updatedProductStr); break;
			case 2: updatedProductStr = updatePrice(updatedProductStr); break;
			case 3: updatedProductStr = updateIncreaseQuantity(updatedProductStr); break;
			case 4: updatedProductStr = updateDecreaseQuantity(updatedProductStr); break;
			case 5: updatedProductStr = updateCategory(updatedProductStr); break;
			default:
			}
			if (option != 6) {
				if (conclude("Finalizar edição (S=Sim / N=Não)? ")) {
					option = 6;
				} else {
					option = showEditOptions();
				}
			}
		}
		return updatedProductStr;
	}
	
	private static double getPrice(String msg) {
		double price = -1d;
		while (price < 0) {
			try {
				price = readDoubleFromUser(msg);
				if (price < 0) {
					throw new NegativeValueException("O preço não pode ser negativo.");
				}
			} catch (NegativeValueException e) {
				System.out.println(e.getMessage());
			}
		}
		return price;
	}
	
	private static int getQuantity(String msg) {
		int quantity = -1;
		while (quantity < 0) {
			try {
				quantity = readIntFromUser(msg);
				if (quantity < 0) {
					throw new NegativeValueException("A quantidade não pode ser negativa.");
				}
			} catch (NegativeValueException e) {
				System.out.println(e.getMessage());;
			}
		}
		return quantity;
	}
	
	private static Map<String, String> updateName(Map<String, String> updatedProductStr) {
		Map<String, String> ProductStr = new HashMap<>();
		ProductStr = updatedProductStr;
		ProductStr.put("name", readNextLineFromUser("Informe o novo nome do produto: "));
		return ProductStr;
	}
	
	private static Map<String, String> updatePrice(Map<String, String> updatedProductStr) {
		updatedProductStr.put("price", Double.toString(getPrice("Informe o seu novo preço: ")));
		return updatedProductStr;
	}
	
	private static Map<String, String> updateIncreaseQuantity(Map<String, String> updatedProductStr) {
		int quantity = Integer.parseInt(updatedProductStr.get("quantity")) + readIntFromUser("Informe quantos items serão adicionados no estoque: ");
		updatedProductStr.put("quantity", Integer.toString(quantity));
		return updatedProductStr;
	}
	
	private static Map<String, String> updateDecreaseQuantity(Map<String, String> updatedProductStr) {
		int decreaseAmount = 1000000000;
		while (decreaseAmount > Integer.parseInt(updatedProductStr.get("quantity"))) {
			try {
				decreaseAmount = readIntFromUser("Informe quantos items serão retirados do estoque: ");
				if (decreaseAmount > Integer.parseInt(updatedProductStr.get("quantity"))) {
					throw new NegativeValueException("A quantidade em estoque não pode ser negativa.");
				}
			} catch (NegativeValueException e) {
				System.out.println(e.getMessage());;
			}
		}
		int quantity = Integer.parseInt(updatedProductStr.get("quantity")) - decreaseAmount;
		updatedProductStr.put("quantity", Integer.toString(quantity));
		return updatedProductStr;
	}
	
	private static Map<String, String> updateCategory(Map<String, String> updatedProductDAO) {
		updatedProductDAO.put("category", readNextLineFromUser("Informe a nova categoria do produto: "));
		return updatedProductDAO;
	}

}
