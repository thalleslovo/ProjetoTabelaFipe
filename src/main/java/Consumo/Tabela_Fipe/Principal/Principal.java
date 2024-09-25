package Consumo.Tabela_Fipe.Principal;

import Consumo.Tabela_Fipe.Service.Consumoapi;
import Consumo.Tabela_Fipe.Service.ConverteDados;
import Consumo.Tabela_Fipe.model.Dados;
import Consumo.Tabela_Fipe.model.Modelos;
import Consumo.Tabela_Fipe.model.Veiculo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1";
    private Consumoapi consumo = new Consumoapi();
    private ConverteDados conversor = new ConverteDados();
    public void exibemenu(){
        var menu = """
                ***Opções***
                
                Carro
                Moto
                Caminhão
                
                Digite  uma das opções para consultar:
                """;
        System.out.println(menu);

        var opcao = leitura.nextLine();
        String endereco;
        if (opcao.toLowerCase().contains("carro")){
            endereco = URL_BASE + "/carros/marcas";
        } else if (opcao.toLowerCase().contains("moto")) {
            endereco = URL_BASE + "motos/marcas";

        }else{
           endereco = URL_BASE + "caminhoes/marcas" ;
        }
        var json = consumo.obterdados(endereco);
        System.out.println(json);
        var marcas = conversor.obterLista(json , Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println(" Informe o código da marca para consulta do veiculo: ");
        var codigomarca = leitura.nextLine();

        endereco = endereco + "/" + codigomarca + "/modelos";
        json = consumo.obterdados(endereco);

        var modeloLista = conversor.obterDados(json , Modelos.class);

        System.out.println("\n Modelos desta marca : ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\n Digite um trecho do nome que você esta buscando : ");
        var nomeveiculo = leitura.nextLine();

        List<Dados> modelosfiltrados = modeloLista.modelos().stream()
                .filter((m-> m.nome().toLowerCase().contains(nomeveiculo.toLowerCase())))
                .collect(Collectors.toList());


        System.out.println("\n Modelos filtrados");
        modelosfiltrados.forEach(System.out::println);

        System.out.println("Digite por favor o código do modelo :");
        var codigoModelo = leitura.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterdados(endereco);
        List<Dados> anos = conversor.obterLista(json , Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i< anos.size(); i++){
            var enderecosAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterdados(enderecosAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);


        }
        System.out.println("\n Todos os veiculos filtrados com avaliação por anos:  ");
        veiculos.forEach(System.out::println);

    }
}
