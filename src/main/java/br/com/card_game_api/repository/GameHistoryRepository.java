package br.com.card_game_api.repository;


import br.com.card_game_api.domain.GameHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para acesso à entidade GameHistory no banco de dados.
 */
@Repository
public interface GameHistoryRepository extends JpaRepository<GameHistory, Long> {
}
