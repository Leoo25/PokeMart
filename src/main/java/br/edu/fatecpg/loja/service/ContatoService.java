package br.edu.fatecpg.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContatoService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmail(String nome, String email, String msg) {

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(nome);
        mensagem.setSubject(email);
        mensagem.setText(msg);

        mailSender.send(mensagem);
    }
}
