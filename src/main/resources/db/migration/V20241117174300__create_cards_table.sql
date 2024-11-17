-- Criacao da tabela cards (cartas)
CREATE TABLE cards (
    id BIGSERIAL PRIMARY KEY,
    value VARCHAR(2) NOT NULL,
    suit VARCHAR(10) NOT NULL
);
