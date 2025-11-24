package br.edu.fatecpg.loja.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.edu.fatecpg.loja.model.Produto;

@Service
public class ProdutoService {
    private final RestTemplate restTemplate;

    public ProdutoService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public Produto[] listarProdutos() {
        String url = "https://yang-ping-screens-waves.trycloudflare.com/itens";
        return restTemplate.getForObject(url, Produto[].class);
   }

    // public List<Map<String, Object>> listarProdutos() {
    //     String url = "https://fakestoreapi.com/products";
    //     return restTemplate.getForObject(url, List.class);
    // }

}
