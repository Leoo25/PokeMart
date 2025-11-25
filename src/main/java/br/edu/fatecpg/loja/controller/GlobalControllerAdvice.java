package br.edu.fatecpg.loja.controller;

import br.edu.fatecpg.loja.model.Item;
import br.edu.fatecpg.loja.model.Usuario;
import br.edu.fatecpg.loja.repository.ItemRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.util.List;

@ControllerAdvice // Essa anotação diz: "Isso vale para TODO o projeto"
public class GlobalControllerAdvice {

    @Autowired
    private ItemRepository itemRepository;

    // @ModelAttribute faz esse método rodar antes de QUALQUER página abrir
    @ModelAttribute
    public void carregarMochilaGlobal(HttpSession session, Model model) {

        // 1. Pega o usuário da sessão
        Usuario usuarioLogado = (Usuario) session.getAttribute("logado");

        // 2. Se tiver alguém logado, carrega os itens
        if (usuarioLogado != null) {
            List<Item> itens = itemRepository.findByUsuarioId(usuarioLogado.getId());

            // 3. Coloca no Model globalmente.
            // Agora a variável ${itens} existe em TODAS as páginas HTML.
            model.addAttribute("itens", itens);
        }
    }
}