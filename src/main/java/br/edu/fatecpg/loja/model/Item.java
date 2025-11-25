package br.edu.fatecpg.loja.model;

import jakarta.persistence.*;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeDoProduto;

    @ManyToOne
    private Usuario usuario;

    private int qtd;

    private String imagemUrl;

    public Item() {}

    public Item(String nomeDoProduto, Usuario usuario) {
        this.nomeDoProduto = nomeDoProduto;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public String getNomeDoProduto() {
        return nomeDoProduto;
    }

    public void setNomeDoProduto(String nomeDoProduto) {
        this.nomeDoProduto = nomeDoProduto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getQtd() { return qtd; }
    public void setQtd(int qtd) { this.qtd = qtd; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }


}