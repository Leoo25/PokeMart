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

@ControllerAdvice 
public class GlobalControllerAdvice {

    @Autowired
    private ItemRepository itemRepository;


    @ModelAttribute
    public void carregarMochilaGlobal(HttpSession session, Model model) {


        Usuario usuarioLogado = (Usuario) session.getAttribute("logado");

    
        if (usuarioLogado != null) {
            List<Item> itens = itemRepository.findByUsuarioId(usuarioLogado.getId());

            
            model.addAttribute("itens", itens);
        }
    }
}
