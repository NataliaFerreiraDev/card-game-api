package br.com.card_game_api.repository;

import br.com.card_game_api.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para acesso à entidade Card no banco de dados.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
