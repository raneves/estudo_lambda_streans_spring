package br.com.romulo.estudo_lambda_streans_spring.principal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.romulo.estudo_lambda_streans_spring.model.DadosEpisodio;
import br.com.romulo.estudo_lambda_streans_spring.model.DadosSerie;
import br.com.romulo.estudo_lambda_streans_spring.model.DadosTemporada;
import br.com.romulo.estudo_lambda_streans_spring.model.Episodio;
import br.com.romulo.estudo_lambda_streans_spring.service.ConsumoAPI;
import br.com.romulo.estudo_lambda_streans_spring.service.ConverteDados;

public class Principal {
	private Scanner leitura = new Scanner(System.in); 
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void exibMenu(){
    		//Never Have I Ever    -> eh um exemplo a ser digitado
    	
    	
            System.out.println("Digite o nome da série para a busca");
            var nomeSerie = leitura.nextLine();
            var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
            
            DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
            System.out.println(dados);
            
            //"https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c"
            
            
            
          List<DadosTemporada> temporadas = new ArrayList<>();
          
          for(int i = 1; i<=dados.totalTemporadas(); i++) {
        	  json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +"&season=" + i + API_KEY);
              DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
              temporadas.add(dadosTemporada);
              
          }
          temporadas.forEach(System.out::println); 
          
          
          for(int i = 0; i < dados.totalTemporadas(); i++){
              List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
              for(int j = 0; j< episodiosTemporada.size(); j++){
            	  System.out.println(episodiosTemporada.get(j).titulo());
              }
	      }
	      temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
	      
	      System.out.println("@@@@@@@@@trabalhando com steams.........");
	      List<String> nomes = Arrays.asList("Fulano", "Beltrano", "Ciclano", "Romulo", "Andressa");
	      
	      
	      nomes.stream()
	          .sorted()//ordenar
	          .forEach(System.out::println);//imprimir
	      
	      nomes.stream()
	          .sorted()
	          .limit(3)//limnitar em 3 objetos
	          .filter(n -> n.startsWith("B")) //tem que comecar com letra B
	          .map(n -> n.toUpperCase())//transforma tudo para maisculo
	          .forEach(System.out::println); //imprime
	      
	      //ou fazer tudo em uma unica linha
	      nomes.stream().sorted().limit(3).filter(n -> n.startsWith("B")).map(n -> n.toUpperCase()).forEach(System.out::println);
	      
	      
	      
	      
	      
	      
	      List<DadosEpisodio> dadosEpisodios = temporadas.stream()
	              .flatMap(t -> t.episodios().stream())//uma lista dentro de uma lista
	              //.toList();//atribuir a uma nova lista, uma lista imutave, nao da para adicionar mais nada
	              .collect(Collectors.toList());//assim conseguimos adicionar novos episodios, como na linha abaixo, com toList nao eh possivel addd
	              
	      dadosEpisodios.add(new DadosEpisodio("teste", 3, "10", "2020-01-01"));
	      dadosEpisodios.forEach(System.out::println);
	      
	      
	      System.out.println("\n Top 5 episódios");
	      dadosEpisodios.stream()
	          .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
	          .limit(5)
	          .forEach(System.out::println);

	      List<Episodio> episodios = temporadas.stream()
	    	        .flatMap(t -> t.episodios().stream()
	    	            .map(d -> new Episodio(t.numero(), d))
	    	        ).collect(Collectors.toList());
	      episodios.forEach(System.out::println);
	      
	      System.out.println("A partir de que ano você deseja ver os episódios? ");
	      var ano = leitura.nextInt();
	      leitura.nextLine();

	      LocalDate dataBusca = LocalDate.of(ano, 1, 1);

	      DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	      episodios.stream()
	      .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
	      .forEach(e -> System.out.println(
	          "Temporada: " + e.getTemporada() +
	              " Episódio: " + e.getTitulo() +
	                  " Data lançamento: " + e.getDataLancamento().format(formatador)
	      ));
    }
}
