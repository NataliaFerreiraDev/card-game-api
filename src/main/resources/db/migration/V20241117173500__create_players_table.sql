-- Criacao da tabela players (jogadores)
CREATE TABLE players (
    id BIGSERIAL PRIMARY KEY,
    identifier VARCHAR(20) NOT NULL,
    score INT NOT NULL
);
