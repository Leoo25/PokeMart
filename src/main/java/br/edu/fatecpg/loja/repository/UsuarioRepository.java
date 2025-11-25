package br.edu.fatecpg.loja.repository;

import br.edu.fatecpg.loja.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByNomeAndSenha(String nome, String senha);
    boolean existsByNome(String nome);
    Optional<Usuario> findByNome(String nome);
    @Query("SELECT u FROM Usuario u JOIN FETCH u.itens WHERE u.id = :id")
    Optional<Usuario> findByIdWithItens(@Param("id") Long id);

}