package br.edu.fatecpg.loja.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean staff;

    private String nome;

    private double dinheiro;

    private String senha;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Item> itens = new ArrayList<>();

    public Usuario(){}

    public Usuario(String nome, boolean staff, double dinheiro, String senha) {
        this.staff = staff;
        this.nome = nome;
        this.dinheiro = dinheiro;
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isStaff() {
        return staff;
    }

    public double getDinheiro() {
        return dinheiro;
    }

    public void setDinheiro(double dinheiro) {
        this.dinheiro = dinheiro;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;


    }

    public void addItem(Item item) {
        itens.add(item);
        item.setUsuario(this);
    }
}