package domain.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository {
	
	public static final String dBFile = "." + File.separator + "pruductsDB.csv";
	
	public List<Map<String, String>> selectAllProducts() {
		return readDBFile();
	}
	
	public List<Map<String, String>> readDBFile() {
		List<Map<String, String>> productsStr = new ArrayList<Map<String, String>>();

		try (BufferedReader br = new BufferedReader(new FileReader(dBFile))) {
			String line = br.readLine();
			if (line == null) {
				return productsStr;
			}
			while (line != null) {
				if ("name".equals(line.substring(0, 4))) {
					line = br.readLine();
					continue;
				}
				String[] content = line.split(",");
				Map<String, String> prodStr = new HashMap<>();
				prodStr.put("name", content[0]);
				prodStr.put("price", content[1]);
				prodStr.put("quantity", content[2]);
				prodStr.put("category", content[3]);
				
				productsStr.add(prodStr);

				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Falha ao tentar encontrar o arquivo do banco de dados.");
		} catch (IOException e) {
			System.out.println("Falha ao obter o caminho do arquivo.");
		}
		return productsStr;
	}

	public String writeDBFile(List<String> productsToSave) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(dBFile, StandardCharsets.UTF_8))) {
			bw.write(new String("name,price,quantity,category"));
			bw.newLine();
			for (String productStr : productsToSave) {
				bw.write(productStr);
				bw.newLine();
			}
			return "Estado da aplicação salvo com sucesso.";
		} catch (IOException e) {
			return "Erro: " + e.getMessage();
		}
	}

}
