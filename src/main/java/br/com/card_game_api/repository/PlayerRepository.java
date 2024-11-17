package br.com.card_game_api.repository;

import br.com.card_game_api.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para acesso à entidade Player no banco de dados.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
