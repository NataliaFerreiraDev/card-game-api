-- Adiciona a coluna game_history_id na tabela players e cria a chave estrangeira
ALTER TABLE players
ADD COLUMN game_history_id BIGINT NOT NULL;

-- Adiciona a chave estrangeira que referencia a tabela game_history
ALTER TABLE players
ADD CONSTRAINT fk_game_history
FOREIGN KEY (game_history_id) REFERENCES game_history(id)
ON DELETE CASCADE;
