package fr.hey.keepmymoney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class KeepMyMoneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeepMyMoneyApplication.class, args);

		// Attendre l'entrée de l'utilisateur pour éviter la fermeture immédiate de la console
		System.out.println("Appuyez sur Entrée pour quitter...");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
	}

}
