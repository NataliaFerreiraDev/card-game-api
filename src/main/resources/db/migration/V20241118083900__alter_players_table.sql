-- Alterando a tabela 'players' para adicionar o campo 'hand' como uma string concatenada
ALTER TABLE players
ADD COLUMN hand TEXT NOT NULL;
