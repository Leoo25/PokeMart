package br.edu.fatecpg.loja.repository;

import br.edu.fatecpg.loja.model.Item;
import br.edu.fatecpg.loja.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long> {
    List<Item> findByUsuarioId(Long id);
}

