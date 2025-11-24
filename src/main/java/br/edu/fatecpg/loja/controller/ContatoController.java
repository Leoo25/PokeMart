package br.edu.fatecpg.loja.controller;

import br.edu.fatecpg.loja.model.Contato;
import br.edu.fatecpg.loja.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    @PostMapping("/contatar")
    public String enviarMensagem(Contato contato, Model model) {

        String assunto = "Nova mensagem de contato: " + contato.getNome();
        String texto =
                "Nome: " + contato.getNome() + "\n" +
                        "Email: " + contato.getEmail() + "\n" +
                        "Mensagem:\n" + contato.getMensagem();

        contatoService.enviarEmail("seuemail@gmail.com", assunto, texto);

        model.addAttribute("msg", "Mensagem enviada com sucesso!");
        return "contato";
    }

}
