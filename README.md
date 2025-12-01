# 🛒 Poké Mart - Sistema de E-commerce Pokémon

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-green) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue)

O **Poké Mart** é uma aplicação Fullstack desenvolvida em Java com Spring Boot, que simula uma loja de itens do universo Pokémon. O projeto está dividido em dois módulos: uma API REST para gestão de inventário e uma aplicação Web para a interface da loja.

## 🔔 Aviso Importante — API Local

A API do projeto roda *localmente* via Spring Boot.  
Isso significa que:

- O frontend *só funciona com API* quando o backend estiver sendo executado.  
- Se a API não estiver online na máquina, partes do sistema como *listar produtos, adicionar itens ou editar* não funcionarão.  

Para usar o sistema completo, é necessário iniciar a API.

## 📋 Funcionalidades

### 🎮 Cliente (Treinador)
- **Registo e Login:** Crie a sua conta de treinador e receba um saldo inicial.
- **Explorar Catálogo:** Visualize itens por categorias (Pokébolas, Medicinais, TMs, etc.).
- **Sistema de Compra:** Adquira itens que são automaticamente descontados do seu saldo e adicionados à sua "Mochila".
- **Mochila:** Visualize o seu inventário pessoal.
- **Contacto:** Formulário funcional para envio de e-mails.

### 🛡️ Admin (Staff)
- **Dashboard:** Visão geral dos utilizadores registados.
- **Gestão de Produtos:** Adicionar, Editar e Remover itens do catálogo global (refletido na API).
- **Gestão de Imagens:** Upload de imagens para os produtos.

## 🛠️ Tecnologias Utilizadas

- **Backend:** Java 17, Spring Boot (Web, Data JPA, Validation).
- **Frontend:** Thymeleaf, HTML5, Tailwind CSS.
- **Base de Dados:** PostgreSQL.
- **Comunicação:** RestTemplate (Consumo de API).
- **Outros:** Lombok, Java Mail Sender.

## 📁 Estrutura do Projeto

/src → código-fonte backend
/target → build da aplicação
/uploads → imagens enviadas pelo usuário
README.md → documentação principal
HELP.md → guia técnico adicional
pom.xml → configuração do Maven
mvnw / mvnw.cmd → Maven wrapper

## ⚙️ Configuração e Instalação

### Pré-requisitos
1.  **Java JDK 17** instalado.
2.  **PostgreSQL** instalado e em execução.
3.  **Maven** (opcional, o projeto inclui o wrapper `mvnw`).

### 1. Configuração da Base de Dados
Aceda ao seu PGAdmin ou terminal PostgreSQL e crie as duas bases de dados necessárias:

```sql
CREATE DATABASE db_itempoke;  -- Para a API
CREATE DATABASE db_loja;      -- Para a Loja
