package br.com.romulo.estudo_lambda_streans_spring.model;

public enum Categoria {
	ACAO("Action"),
	COMEDIA("Comedy"),
	DRAMA("Drama"),
	ROMANCE("Romance"),
	CRIME("Crime");
	
	private String categoriaOmdb;

    Categoria(String categoriaOmdb){
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
