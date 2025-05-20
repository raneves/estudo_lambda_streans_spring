package br.com.romulo.estudo_lambda_streans_spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.romulo.estudo_lambda_streans_spring.model.DadosEpisodio;
import br.com.romulo.estudo_lambda_streans_spring.model.DadosSerie;
import br.com.romulo.estudo_lambda_streans_spring.model.DadosTemporada;
import br.com.romulo.estudo_lambda_streans_spring.service.ConsumoAPI;
import br.com.romulo.estudo_lambda_streans_spring.service.ConverteDados;

@SpringBootApplication
public class EstudoLambdaStreansSpringApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(EstudoLambdaStreansSpringApplication.class, args);
	}

	@Override  //sera o methodo main
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println("Primeiro projeto spring sem web");
		var consumoApi = new ConsumoAPI();
	    var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
//				System.out.println(json);
//				json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
	    System.out.println(json);
	    ConverteDados conversor = new ConverteDados();
	    DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
	    System.out.println(dados);
	    
	    System.out.println(dados);
        json = consumoApi.obterDados("https://omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=6585022c");
        DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
        System.out.println(dadosEpisodio);
        
        List<DadosTemporada> temporadas = new ArrayList<>();
        
        for(int i = 1; i<=dados.totalTemporadas(); i++) {
            json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=6585022c");
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
            
        }
        temporadas.forEach(System.out::println);    
	}
}
