package br.com.romulo.estudo_lambda_streans_spring.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {
	
	public String obterDados(String endereco) {
	    HttpClient client = HttpClient.newHttpClient(); //cliente
	    HttpRequest request = HttpRequest.newBuilder() //request
	            .uri(URI.create(endereco))
	            .build();
	    HttpResponse<String> response = null;
	    try {//como receber a resposta
	        response = client
	                .send(request, HttpResponse.BodyHandlers.ofString());
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    } catch (InterruptedException e) {
	        throw new RuntimeException(e);
	    }

	    String json = response.body(); //devolver o corpo da resposta
	    return json;
	}
}
