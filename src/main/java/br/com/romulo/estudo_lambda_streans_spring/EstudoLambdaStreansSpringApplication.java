package br.com.romulo.estudo_lambda_streans_spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.romulo.estudo_lambda_streans_spring.principal.Principal;

@SpringBootApplication
public class EstudoLambdaStreansSpringApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(EstudoLambdaStreansSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();
	}
}
