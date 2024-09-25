package Consumo.Tabela_Fipe.Service;


import java.util.List;

public interface IconverteDados {
    <T> T obterDados(String json, Class<T> classe);

    <T> List<T> obterLista(String json, Class<T> classe);
}
