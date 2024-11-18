-- Renomeia a coluna player_count para cardsPerPlayer na tabela game_history
ALTER TABLE game_history
RENAME COLUMN player_count TO number_of_players;
