package br.com.romulo.estudo_lambda_streans_spring.principal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import br.com.romulo.estudo_lambda_streans_spring.model.DadosEpisodio;
import br.com.romulo.estudo_lambda_streans_spring.model.DadosSerie;
import br.com.romulo.estudo_lambda_streans_spring.model.DadosTemporada;
import br.com.romulo.estudo_lambda_streans_spring.model.Episodio;
import br.com.romulo.estudo_lambda_streans_spring.service.ConsumoAPI;
import br.com.romulo.estudo_lambda_streans_spring.service.ConverteDados;

public class Principal {

    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoAPI consumo = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    private DadosSerie dadosSerie;
    private String nomeSerie;
    private List<DadosTemporada> temporadas;

    public void exibMenu() {
        buscarSerie();
        buscarTemporadas();
        exibirTitulosEpisodios();
        demonstrarStreamBasico();
        List<DadosEpisodio> dadosEpisodios = obterTodosEpisodios();
        exibirTop10Episodios(dadosEpisodios);
        List<Episodio> episodios = converterParaEpisodiosComTemporada(dadosEpisodios);
        filtrarPorAno(episodios);
        buscarPorTitulo(episodios);
        mapearAvaliacoesPorTemporada(episodios);
        coletandoEstatisticas(episodios);
    }

    private void buscarSerie() {
        System.out.println("Digite o nome da série para a busca:");
        nomeSerie = leitura.nextLine(); // Lê nome da série do usuário
        String json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY); // Busca dados da API
        dadosSerie = conversor.obterDados(json, DadosSerie.class); // Converte JSON para DadosSerie
        System.out.println(dadosSerie);
    }

    private void buscarTemporadas() {
        temporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            String json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY); // Busca temporada
            DadosTemporada temporada = conversor.obterDados(json, DadosTemporada.class); // Converte JSON para temporada
            temporadas.add(temporada);
        }
    }

    private void exibirTitulosEpisodios() {
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo()))); // Exibe títulos dos episódios
    }

    private void demonstrarStreamBasico() {
        System.out.println("\n@@@@@@@@@ Trabalhando com streams...");
        List<String> nomes = Arrays.asList("Fulano", "Beltrano", "Ciclano", "Romulo", "Andressa");

        nomes.stream().sorted().forEach(System.out::println); // Ordena e imprime nomes

        nomes.stream() // Inicia o stream da lista
                .sorted() // Ordena os elementos
                .limit(3) // Limita aos 3 primeiros
                .filter(n -> n.startsWith("B")) // Filtra os que começam com B
                .map(String::toUpperCase) // Converte para maiúsculas
                .forEach(System.out::println); // Imprime os resultados
    }

    private List<DadosEpisodio> obterTodosEpisodios() {
        List<DadosEpisodio> todos = temporadas.stream() // Stream das temporadas
                .flatMap(t -> t.episodios().stream()) // Junta todos episódios
                .collect(Collectors.toList()); // Coleta em uma lista

        todos.add(new DadosEpisodio("teste", 3, "10", "2020-01-01"));
        return todos;
    }

    private void exibirTop10Episodios(List<DadosEpisodio> episodios) {
        System.out.println("\nTop 10 episódios:");
        episodios.stream() // Inicia stream de episódios
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A")) // Remove sem avaliação
                .peek(e -> System.out.println("Primeiro filtro (N/A): " + e)) // Exibe após filtro
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed()) // Ordena por avaliação desc
                .peek(e -> System.out.println("Ordenação: " + e)) // Exibe ordenados
                .limit(10) // Limita aos 10 primeiros
                .peek(e -> System.out.println("Limite: " + e)) // Exibe top 10
                .map(e -> e.titulo().toUpperCase()) // Converte título para maiúsculo
                .peek(e -> System.out.println("Mapeamento: " + e)) // Exibe título final
                .forEach(System.out::println); // Imprime no console
    }

    private List<Episodio> converterParaEpisodiosComTemporada(List<DadosEpisodio> dadosEpisodios) {
        return temporadas.stream()
                .flatMap(t -> t.episodios().stream() // Stream de episódios
                        .map(d -> new Episodio(t.numero(), d))) // Adiciona número da temporada
                .collect(Collectors.toList()); // Coleta na lista final
    }

    private void filtrarPorAno(List<Episodio> episodios) {
        System.out.println("\nA partir de que ano você deseja ver os episódios?");
        int ano = leitura.nextInt();
        leitura.nextLine(); // limpar buffer
        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca)) // Filtra por data
                .forEach(e -> System.out.println("Temporada: " + e.getTemporada() // Exibe episódio filtrado
                        + " Episódio: " + e.getTitulo()
                        + " Data lançamento: " + e.getDataLancamento().format(formatador)));
    }

    private void buscarPorTitulo(List<Episodio> episodios) {
        System.out.println("\nDigite um trecho do título do episódio:");
        String trecho = leitura.nextLine();

        Optional<Episodio> episodio = episodios.stream() // Stream de episódios
                .filter(e -> e.getTitulo().toUpperCase().contains(trecho.toUpperCase()))
                .findFirst();

        episodio.ifPresentOrElse( // Executa se presente ou ausente
                e -> {
                    System.out.println("Episódio encontrado!");
                    System.out.println("Temporada: " + e.getTemporada());
                },
                () -> System.out.println("Episódio não encontrado!")
        );
    }

    private void mapearAvaliacoesPorTemporada(List<Episodio> episodios) {
        System.out.println("\nMédia de avaliações por temporada:");
        Map<Integer, Double> avaliacoes = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0) // Apenas com avaliação válida
                .collect(Collectors.groupingBy(Episodio::getTemporada, // Agrupa por temporada
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        avaliacoes.forEach((temp, media) -> System.out.println("Temporada " + temp + ": " + media)); // Imprime média
    }

    private void coletandoEstatisticas(List<Episodio> episodios) {
        DoubleSummaryStatistics est = episodios.stream() // Stream de episódios
                .filter(e -> e.getAvaliacao() > 0.0) // Filtra avaliações válidas
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao)); // Estatísticas das avaliações
        System.out.println(est);
        //sera impresso:
        //DoubleSummaryStatistics{count=70, sum=612,500000, min=4,000000, average=8,750000, max=9,900000}
        //count: 70 valores avaliados
        //sum: soma total das avaliações (612,5)
       //min / max: menor (4,0) e maior (9,9) avaliação
       //average: média das avaliações (8,75)
        
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());
    }
}
