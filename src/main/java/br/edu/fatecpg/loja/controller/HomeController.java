package br.edu.fatecpg.loja.controller;

import br.edu.fatecpg.loja.model.Item;
import br.edu.fatecpg.loja.model.Usuario;
import br.edu.fatecpg.loja.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @GetMapping("/")
    public String index (){
        return "index";
    }

    @GetMapping("/cadastro")
    public String cadastro (HttpSession session) {
        if (session.getAttribute("logado") != null) {
            return "redirect:/";
        }
            return "cadastro";
    }

    @GetMapping("/contato")
    public String contato (){
        return "contato";
    }

    @GetMapping("/login")
    public String login (HttpSession session) {
        if (session.getAttribute("logado") != null) {
            return "redirect:/";
        }
            return "login";

    }


}
