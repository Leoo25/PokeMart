package br.edu.fatecpg.loja.controller;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import br.edu.fatecpg.loja.model.Produto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

@Controller
public class ApiController {
    public static class Item {
        private String name;
        @JsonProperty("image_url")
        private String image;

        private String imagem;


        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }

        public String getImagem() { return imagem; }
        public void setImagem(String imagem) { this.imagem = imagem; }
    }

    @GetMapping("/consumirApi")
    public String consumirApi(Model model) {
        String url = "https://netscape-icons-terminal-snowboard.trycloudflare.com/itens";
        RestTemplate restTemplate = new RestTemplate();

        try {
            List<Map<String, Object>> resposta =
                    restTemplate.getForObject(url, List.class);

            model.addAttribute("response", resposta);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erro ao consumir API: " + e.getMessage());
        }

        return "produtos";
    }

    @GetMapping("/categoria")
    public String consumirporCategoria(@RequestParam(required = false) String categoria, Model model) {
        String url = "https://netscape-icons-terminal-snowboard.trycloudflare.com/itens";
        RestTemplate restTemplate = new RestTemplate();

        try {

            Produto[] todosItens = restTemplate.getForObject(url, Produto[].class);


            Set<String> categoriasUnicas = Arrays.stream(todosItens)
                    .filter(p -> p.getCategoria() != null && p.getCategoria().getNome() != null)
                    .map(p -> p.getCategoria().getNome())
                    .collect(Collectors.toSet());

            model.addAttribute("listaCategorias", categoriasUnicas);


            List<Produto> listaFiltrada;


            if (categoria != null && !categoria.isEmpty()) {
                listaFiltrada = Arrays.stream(todosItens)
                        .filter(p -> p.getCategoria() != null && p.getCategoria().getNome() != null)
                        .filter(p -> categoria.equalsIgnoreCase(p.getCategoria().getNome()))
                        .collect(Collectors.toList());

                model.addAttribute("categoriaSelecionada", categoria);
            } else {
                listaFiltrada = Arrays.asList(todosItens);
            }

            model.addAttribute("response", listaFiltrada);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erro: " + e.getMessage());
        }

        return "produtos";
    }


    @PostMapping("/uploadImagem")
    public String uploadImagem(@RequestParam("id") Long id,
                               @RequestParam("imagem") MultipartFile file) {
        if (file.isEmpty()) {
            System.out.println("Nenhum arquivo enviado!");
            return "redirect:/consumirApi";
        }
        try {
            String caminho = System.getProperty("user.dir") + "/uploads/";
            File pasta = new File(caminho);
            if (!pasta.exists()) pasta.mkdirs();
            String extensao = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));
            String nomeArquivo = "user_" + id + extensao;
            file.transferTo(new File(caminho + nomeArquivo));
            System.out.println("Imagem salva: " + nomeArquivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/consumirApi";
    }


}
