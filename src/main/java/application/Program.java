package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import domain.repository.Repository;
import frontend.CenterFacade;
import frontend.UserInteraction;

public class Program {

	public static void main(String[] args) {
		
		Locale.setDefault(Locale.US);
		
		CenterFacade facade = new CenterFacade();
		
		int option = 0;
		System.out.println("Bem-vindo à Loja Online\n\n");
		
		// Verificando existência do arquivo de banco de dados e criando se não existir
		BufferedWriter out = null;
		try (BufferedReader br = new BufferedReader(new FileReader(Repository.dBFile))) {
		} catch (IOException e) {
			try {
				out = new BufferedWriter(new FileWriter("pruductsDB.csv"));
				System.out.println("Inicializando arquivo de banco de dados do programa.\n");
			} catch (IOException e1) {
				System.out.println("Falha ao inicializar o arquivo de banco de dados.");
			} finally {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		while(option != 5) {
			option = UserInteraction.showMainOptions();
			facade.mainMenu(option);
		}

	}

}
