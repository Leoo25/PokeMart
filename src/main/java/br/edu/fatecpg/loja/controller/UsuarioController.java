package br.edu.fatecpg.loja.controller;

import br.edu.fatecpg.loja.model.Usuario;
import br.edu.fatecpg.loja.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/listar")
    public String listarUsuarios (HttpSession session, Model model){
        Usuario logado = (Usuario) session.getAttribute("logado");
        if(logado == null) {
            return "redirect:/login";
        }
        if (!logado.isStaff()) {
            return "redirect:/";
        }
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios";
    }

    @GetMapping("/dashboard")
    public String dashboard (HttpSession session, Model model){
        Usuario logado = (Usuario) session.getAttribute("logado");
        if(logado == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "dashboard";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return "redirect:/listar";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));

        model.addAttribute("usuario", usuario);
        return "editarUsuario";
    }

    @PostMapping("/atualizar")
    public String atualizar(Usuario usuario) {
        usuarioRepository.save(usuario);
        return "redirect:/listar";
    }

    @PostMapping("/cadastrar")
    public String cadastrar (@RequestParam String nome,
                             @RequestParam String senha,Model model) {
        if(usuarioRepository.existsByNome(nome)) {
            model.addAttribute("mensagem", "Usuário já existe!");
            return "cadastro";
        }else{
            Usuario user = new Usuario(nome, false, 50000.00, senha);

            usuarioRepository.save(user);
            model.addAttribute("mensagem", "Usuário cadastrado com sucesso!");
            return "cadastro";
        }

    }

    @PostMapping("/logar")
    public String logar (@RequestParam String nome, @RequestParam String senha, HttpSession session, Model model){

        Optional<Usuario> usuario = usuarioRepository.findByNomeAndSenha(nome, senha);
        if(usuario.isPresent()){
            session.setAttribute("logado", usuario.get());
            model.addAttribute("mensagem", "Login realizado com sucesso!");
            return "index";
        }else{
            model.addAttribute("mensagem", "Usuário ou senha inválidos!");
            model.addAttribute("erro", true);
            return "login";
        }

    }
    @GetMapping("/logout")
    public String logout (HttpSession session) {
        session.invalidate();
        return "index";
    }

}
