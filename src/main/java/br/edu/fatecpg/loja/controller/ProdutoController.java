package br.edu.fatecpg.loja.controller;

import br.edu.fatecpg.loja.model.Item;
import br.edu.fatecpg.loja.model.Produto;
import br.edu.fatecpg.loja.model.Usuario;
import br.edu.fatecpg.loja.repository.ItemRepository;
import br.edu.fatecpg.loja.repository.UsuarioRepository;
import br.edu.fatecpg.loja.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final RestTemplate restTemplate;
    private final ProdutoService produtoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ItemRepository itemRepository;

    public ProdutoController(RestTemplateBuilder builder, ProdutoService produtoService) {
        this.restTemplate = builder.build();
        this.produtoService = produtoService;
    }

    @GetMapping("/novo")
    public String novoProdutoForm() {
        return "produto-form";
    }

    @PostMapping("/adicionar")
    public String adicionarProduto(
            @RequestParam String nome,
            @RequestParam String descricao,
            @RequestParam double preco,
            @RequestParam int qtd,
            @RequestParam String categoria,
            @RequestParam("imagem") MultipartFile imagem
    ) {
        Path caminhoTemporario = null;
        try {
            Files.createDirectories(Paths.get("uploads"));
            String nomeArquivo = UUID.randomUUID() + "-" + imagem.getOriginalFilename();
            caminhoTemporario = Paths.get("uploads/" + nomeArquivo);
            Files.write(caminhoTemporario, imagem.getBytes());

            String apiUrl = "https://netscape-icons-terminal-snowboard.trycloudflare.com/itens";

            Map<String, Object> dadosObj = new HashMap<>();
            dadosObj.put("nome", nome);
            dadosObj.put("descricao", descricao);
            dadosObj.put("preco", preco);
            dadosObj.put("categoria", categoria);
            dadosObj.put("qtd", qtd);

            ObjectMapper mapper = new ObjectMapper();
            String dadosJson = mapper.writeValueAsString(dadosObj);

            HttpHeaders jsonHeaders = new HttpHeaders();
            jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> jsonEntity = new HttpEntity<>(dadosJson, jsonHeaders);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("dados", jsonEntity);

            FileSystemResource fileResource = new FileSystemResource(caminhoTemporario.toFile());
            body.add("imagem", fileResource);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            restTemplate.postForEntity(apiUrl, request, String.class);


            if (caminhoTemporario != null) {
                try { Files.deleteIfExists(caminhoTemporario); } catch (Exception e) { /* Ignora erro de arquivo preso */ }
            }

            return "redirect:/produtos/novo?success";

        } catch (Exception e) {
            e.printStackTrace();
            if (caminhoTemporario != null) {
                try { Files.deleteIfExists(caminhoTemporario); } catch (Exception ex) {}
            }
            return "redirect:/produtos/novo?error";
        }
    }

    @GetMapping("/editar/{id}")
    public String abrirFormularioEdicao(@PathVariable Long id, Model model) {
        try {
            String apiUrl = "https://netscape-icons-terminal-snowboard.trycloudflare.com/itens/" + id;

            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
            model.addAttribute("produto", response.getBody());

            return "editar-produto";

        } catch (Exception e) {
            e.printStackTrace();

            return "redirect:/consumirApi?error=NaoEncontrado";
        }
    }

    @PostMapping("/editar")
    public String editarProduto(
            @RequestParam Long id,
            @RequestParam String nome,
            @RequestParam String descricao,
            @RequestParam String preco,
            @RequestParam int qtd,
            @RequestParam String categoria,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem
    ) {
        Path caminhoTemporario = null;

        try {

            double precoDouble = Double.parseDouble(preco.replace(",", "."));

            String apiUrl = "https://netscape-icons-terminal-snowboard.trycloudflare.com/itens/" + id;

            Map<String, Object> dadosObj = new HashMap<>();
            dadosObj.put("nome", nome);
            dadosObj.put("descricao", descricao);
            dadosObj.put("preco", precoDouble);
            dadosObj.put("categoria", categoria);
            dadosObj.put("qtd", qtd);

            ObjectMapper mapper = new ObjectMapper();
            String dadosJson = mapper.writeValueAsString(dadosObj);

            HttpHeaders jsonHeaders = new HttpHeaders();
            jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> jsonEntity = new HttpEntity<>(dadosJson, jsonHeaders);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("dados", jsonEntity);

            if (imagem != null && !imagem.isEmpty()) {
                Files.createDirectories(Paths.get("uploads"));
                String nomeArquivo = UUID.randomUUID() + "-" + imagem.getOriginalFilename();
                caminhoTemporario = Paths.get("uploads/" + nomeArquivo);
                Files.write(caminhoTemporario, imagem.getBytes());

                FileSystemResource fileResource = new FileSystemResource(caminhoTemporario.toFile());
                body.add("imagem", fileResource);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            restTemplate.exchange(apiUrl, HttpMethod.PUT, request, String.class);


            if (caminhoTemporario != null) {
                try { Files.deleteIfExists(caminhoTemporario); } catch (Exception ignored) { }
            }

            return "redirect:/consumirApi?successEdit";

        } catch (Exception e) {
            e.printStackTrace();

            if (caminhoTemporario != null) {
                try { Files.deleteIfExists(caminhoTemporario); } catch (Exception ignored) {}
            }

            return "redirect:/consumirApi?error=ErroAoEditar";
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletarProduto(@PathVariable Long id) {
        try {
            String apiUrl = "https://netscape-icons-terminal-snowboard.trycloudflare.com/itens/" + id;
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> request = new HttpEntity<>(headers);

            restTemplate.exchange(apiUrl, HttpMethod.DELETE, request, Void.class);

            return "redirect:/consumirApi?successDelete";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/consumirApi?errorDelete";
        }
    }

    @PostMapping("/diminuir")
    public String diminuirQuantidade(@RequestParam Long id, HttpSession session) {
        try {
            Usuario usuarioDaSessao = (Usuario) session.getAttribute("logado");

            if (usuarioDaSessao == null) {
                return "redirect:/login";
            }

            String baseUrl = "https://netscape-icons-terminal-snowboard.trycloudflare.com/itens/";

            Produto itemApi;
            try {
                itemApi = restTemplate.getForObject(baseUrl + id, Produto.class);
            } catch (HttpClientErrorException.NotFound e) {
                return "redirect:/consumirApi?errorItemNotFound";
            }

            Usuario usuario = usuarioRepository.findById(usuarioDaSessao.getId()).orElseThrow();


            if (usuario.getDinheiro() < itemApi.getPreco()) {
                return "redirect:/consumirApi?errorSemSaldo";
            }


            Map<String, Integer> body = new HashMap<>();
            body.put("qtd", 1);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Integer>> request = new HttpEntity<>(body, headers);

            restTemplate.postForEntity(baseUrl + id + "/comprar", request, String.class);


            usuario.setDinheiro(usuario.getDinheiro() - itemApi.getPreco());

            Item itemExistente = null;


            for (Item item : usuario.getItens()) {
                if (item.getNomeDoProduto().equals(itemApi.getNome())) {
                    itemExistente = item;
                    break;
                }
            }

            if (itemExistente != null) {

                itemExistente.setQtd(itemExistente.getQtd() + 1);
            } else {

                Item novoItem = new Item();
                novoItem.setNomeDoProduto(itemApi.getNome());
                novoItem.setImagemUrl(itemApi.getImagemUrl());
                novoItem.setUsuario(usuario);
                novoItem.setQtd(1);
                usuario.getItens().add(novoItem);

            }




            usuarioRepository.save(usuario);
            session.setAttribute("logado", usuario);

            return "redirect:/consumirApi?updated";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/consumirApi?errorUpdate";
        }
    }

    @GetMapping("/inventario")
    public String inventario(HttpSession session) {

        if (session.getAttribute("logado") == null) {
            return "redirect:/login";
        }

        return "inventario";
    }}
