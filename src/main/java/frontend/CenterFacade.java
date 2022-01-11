package frontend;

import static frontend.ProductController.setAtributesString;
import static frontend.ProductController.setNewProductString;
import static frontend.UserInteraction.conclude;
import static frontend.UserInteraction.readNextFromUser;
import static frontend.UserInteraction.readOption;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import domain.entities.Product;
import exceptions.NegativeValueException;
import exceptions.ThereAreNoProductsException;
import services.CenterService;

public class CenterFacade {

	private static CenterService centerService;

	public CenterFacade() {
		centerService = new CenterService();
	}

	public void mainMenu(int option) {
		switch (option) {
		case 1:
			System.out.println(addProduct());
			clearScreen();
			break;
		case 2:
			System.out.println(updateProduct());
			clearScreen();
			break;
		case 3:
			System.out.println(deleteProduct());
			clearScreen();
			break;
		case 4:
			System.out.println(importProducts());
			clearScreen();
			break;
		default:
			System.out.println("Aplicação encerrada.");
			clearScreen();
		}
	}

	private String addProduct() {
		Map<String, String> productStr = setNewProductString();
		boolean confirm = conclude("Confirmar a adição do produto (S=Sim / N=Não)? ");
		if (confirm) {
			try {
				centerService.performAddProduct(productStr);
				return "Produto adicionado com sucesso.";
			} catch (NegativeValueException e) {
				return "Erro: falha na adição de produto à base de dados. " + e.getMessage();
			}
		} else {
			return "Adição de produto cancelada";
		}
	}

	private String updateProduct() {
		List<Product> products = centerService.performGetAllProducts();
		Product product;
		try {
			product = viewProductsAndSelectOne(products);
		} catch (ThereAreNoProductsException e) {
			return e.getMessage();
		}
		if (product == null) {
			return "";
		}

		int position = products.indexOf(product);
		Map<String, String> updatedProductStr = setAtributesString(product);

		boolean confirm = conclude("Confirmar a edição do produto (S=Sim / N=Não)? ");
		if (confirm) {
			try {
				centerService.performUpdateProduct(position, updatedProductStr);
				return "Produto editado com sucesso.";
			} catch (NegativeValueException e) {
				return "Erro: falha na atualização de produto na base de dados. " + e.getMessage();
			}
		} else {
			return "Edição de produto cancelada";
		}
	}

	private String deleteProduct() {
		List<Product> products = centerService.performGetAllProducts();
		Product product;
		try {
			product = viewProductsAndSelectOne(products);
		} catch (ThereAreNoProductsException e) {
			return e.getMessage();
		}
		if (product == null) {
			return "";
		}

		int position = products.indexOf(product);

		boolean confirm = conclude("Confirmar a exclusão do produto (S=Sim / N=Não)? ");
		if (confirm) {
			centerService.performDeleteProduct(position);
			return "Produto excluído com sucesso.";
		} else {
			return "Exclusão de produto cancelada";
		}
	}

	private String importProducts() {
		String path = readNextFromUser("Digite o caminho do arquivo para importação: ");
		List<Product> newProducts = null;
		try {
			newProducts = centerService.performLoadNewProducts(path);
		} catch (FileNotFoundException e) {
			return "Falha ao tentar encontrar o arquivo.";
		} catch (IOException e) {
			return "Falha ao obter o caminho do arquivo.";
		}

		System.out.println();
		System.out.println(newProducts.size() + " produtos a serem importados.");

		if (newProducts.size() > 0) {
			boolean confirm = conclude("Confirmar a importação de produtos (S=Sim / N=Não)? ");
			if (confirm) {
				centerService.performImportProduct(newProducts);
				return "Importação de produtos realizada com sucesso.";
			} else {
				return "Importação de produtos cancelada.";
			}
		} else {
			return "Importação de produtos não realizada.";
		}
	}

	private Product viewProductsAndSelectOne(List<Product> products) throws ThereAreNoProductsException {
		if (products == null || products.size() == 0) {
			throw new ThereAreNoProductsException("Não constam produtos cadastrados.\n");
		}

		System.out.println();
		for (int i = 1; i <= products.size(); i++) {
			System.out.println("[" + i + "] para selecionar => " + products.get(i - 1));
		}
		System.out.println("[" + (products.size() + 1) + "] para cancelar a operação.");

		int select = readOption("Digite o número do produto a ser selecionado: ", 1, products.size() + 1);
		if (select == products.size() + 1) {
			System.out.println("Operação cancelada.");
			return null;
		}
		return products.get(select - 1);
	}

	private void clearScreen() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.println("Error: " + e.getMessage());
		}
		System.out.println("\n\n\n\n\n\n\n\n\n\n");
	}

}
