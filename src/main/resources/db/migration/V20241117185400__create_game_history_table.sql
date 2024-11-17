-- Criacao da tabela de historico de jogos
CREATE TABLE game_history (
    id BIGSERIAL PRIMARY KEY,
    player_count INT NOT NULL,
    cards_per_player INT NOT NULL,
    deck_id VARCHAR(50) NOT NULL,
    winner VARCHAR(50) NOT NULL,
    highest_score INT NOT NULL,
    game_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
