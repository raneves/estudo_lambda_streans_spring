package br.com.romulo.estudo_lambda_streans_spring.service;

public interface IConverteDados {
	<T> T  obterDados(String json, Class<T> classe);
}
