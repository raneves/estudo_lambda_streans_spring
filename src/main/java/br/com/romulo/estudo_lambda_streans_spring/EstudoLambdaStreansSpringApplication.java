package br.com.romulo.estudo_lambda_streans_spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EstudoLambdaStreansSpringApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(EstudoLambdaStreansSpringApplication.class, args);
	}

	@Override  //sera o methodo main
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Primeiro projeto spring sem web");
	}

}
