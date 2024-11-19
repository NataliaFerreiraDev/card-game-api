# **Card Game API**

Este projeto é uma API RESTful desenvolvida para o desafio de um processo seletivo.
O objetivo é criar um baralho de cartas, distribuir as cartas entre os jogadores e calcular a "mão" com a maior
somatória de valores, levando em consideração um sistema de pontuação baseado em valores específicos para cartas
como A, K, Q e J.

## Desafio

O desafio consiste em criar um baralho de cartas e montar um jogo de cartas, onde:

- O número de jogadores e o número de cartas por mão devem ser informados como parâmetros.
- A aplicação deve verificar qual jogador tem a maior somatória de pontos na sua mão.
- Em caso de empate, a aplicação retorna os vencedores empatados.

### Regras de Pontuação

As cartas possuem os seguintes valores:

- A (Ás) = 1
- K (Rei) = 13
- Q (Rainha) = 12
- J (Valete) = 11

Outras cartas numéricas possuem seu próprio valor, conforme o número impresso nelas.

### Requisitos

- Spring Boot / Java 8+
- Padrão REST API
- Persistência em banco de dados (PostgreSQL)
- Docker para configuração do banco de dados
- Não utilizar Lombok
- Testes unitários
- Comandos descritivos no Git

## Funcionalidades

- **Distribuição de cartas**: A API integra com a [Deck of Cards API](https://deckofcardsapi.com/) para gerar um baralho de cartas.
- **Jogo de cartas**: Permite especificar o número de jogadores e o número de cartas por jogador, distribuindo as cartas e calculando a somatória dos pontos de cada jogador.
- **Determinação do vencedor**: A API retorna o jogador (ou jogadores, em caso de empate) com a maior pontuação.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.5**
- **PostgreSQL**
- **Flyway (para migração de banco de dados)**
- **Docker** (para configuração e execução do banco de dados)
- **JUnit 5 e Mockito** (para testes unitários)
- **Arquitetura Hexagonal** (para a organização e separação de responsabilidades)

## Como Rodar o Projeto

### Pré-requisitos

Antes de rodar o projeto, você precisa ter o **Docker** e o **Docker Compose** instalados no seu ambiente local. Não é necessário instalar Java, Maven ou PostgreSQL, pois esses serviços serão gerenciados automaticamente pelos containers Docker.

- **Docker**: Para criar e rodar os containers da aplicação e do banco de dados.
- **Docker Compose**: Para orquestrar a criação e execução dos containers.

### 1. Clonar o Repositório

Primeiro, clone o repositório do projeto para sua máquina local:

```bash
git clone https://github.com/seu-usuario/card-game-api.git
```

### 2. Construir a Aplicação

Com o repositório clonado, o próximo passo é construir a aplicação. Se você optar por usar **Docker**, esse passo será tratado automaticamente pelo `docker-compose`. Caso contrário, você pode usar o **Maven** para gerar o arquivo JAR da aplicação.

#### Com Docker:

Para construir a aplicação utilizando o **Docker**, execute o comando abaixo:

```bash
docker-compose build
```

Esse comando irá criar a imagem Docker para a aplicação com base no arquivo `Dockerfile` presente no repositório.

### 3. Iniciar o Docker com o Banco de Dados e a Aplicação

Depois de construir a aplicação, o próximo passo é inicializar os containers. Esse comando criará e iniciará os containers para a aplicação e o banco de dados PostgreSQL conforme configurado no arquivo `docker-compose.yml`.

Execute o seguinte comando:

```bash
docker-compose up -d
```

- **Container da aplicação**: Será iniciado na porta `8080`.
- **Container do PostgreSQL**: Será configurado e iniciado automaticamente.

Após a execução do comando, a aplicação estará disponível em `http://localhost:8080`.

### 4. Acessar o Swagger para Documentação Interativa

A aplicação vem com o **Swagger UI**, que fornece uma documentação interativa para a API. 
Acesse o Swagger UI no seguinte endereço para explorar as rotas e testar os endpoints da aplicação:
```bash
http://localhost:8080/swagger-ui.html
```

No **Swagger**, você poderá:

- Consultar as rotas disponíveis.
- Ver exemplos de requisição e resposta.
- Interagir diretamente com a API para testar os endpoints.


### 5. Monitoramento e Métricas com Spring Actuator

Este projeto utiliza o **Spring Actuator** para fornecer métricas e informações sobre o estado de saúde da aplicação.
O **Spring Actuator** é uma excelente ferramenta para monitoramento e observabilidade, facilitando a visualização
do comportamento da aplicação em produção.
Neste projeto, o seguinte endpoint está disponível:
- **/actuator/health**: Exibe o estado de saúde da aplicação (UP, DOWN, etc.). Este endpoint é útil para monitorar se a aplicação está funcionando corretamente.

```bash
http://localhost:8080/actuator/health
```

## Endpoints

### POST /game/play
Inicia um novo jogo, informando o número de jogadores e o número de cartas por jogador. Retorna o histórico do jogo com as cartas distribuídas e as pontuações de cada jogador.

#### Parâmetros de Entrada:
- `numPlayers` (inteiro, obrigatório): Número de jogadores no jogo (mínimo 1).
- `cardsPerHand` (inteiro, obrigatório): Número de cartas distribuídas para cada jogador (mínimo 1).


### Exemplo de Requisição:
```markdown
POST http://localhost:8080/game/play
```

**Corpo da Requisição:**

```json
{
  "numPlayers": 4,
  "cardsPerHand": 5
}
```

#### Parâmetros de Saída:
- `id` (inteiro): ID único do jogo.
- `numberOfPlayers` (inteiro): Número de jogadores no jogo.
- `cardsPerPlayer` (inteiro): Número de cartas distribuídas para cada jogador.
- `deckId` (string): ID do baralho utilizado.
- `winner` (string): Nome do vencedor do jogo.
- `highestScore` (inteiro): Maior pontuação alcançada no jogo.
- `gameTimestamp` (string, data): Data e hora em que o jogo foi iniciado, no formato ISO 8601.

#### Exemplo de Resposta:
```json
{
    "id": 2,
    "numberOfPlayers": 6,
    "cardsPerPlayer": 5,
    "deckId": "mmtepja8n2n1",
    "winner": "Jogador 4",
    "highestScore": 40,
    "gameTimestamp": "2024-11-19T11:35:50.24282836"
}
```

### GET /game/history/{gameId}
Consulta o histórico de um jogo específico, identificado pelo ID do jogo.

#### Parâmetros de Entrada:
- `gameId` (inteiro, obrigatório): ID único do jogo.

### Exemplo de Requisição:

```markdown
GET http://localhost:8080/game/history/1
```

#### Parâmetros de Saída:
- `id` (inteiro): ID único do jogo.
- `numberOfPlayers` (inteiro): Número de jogadores no jogo.
- `cardsPerPlayer` (inteiro): Número de cartas distribuídas para cada jogador.
- `deckId` (string): ID do baralho utilizado.
- `winner` (string): Nome do vencedor do jogo.
- `highestScore` (inteiro): Maior pontuação alcançada no jogo.
- `gameTimestamp` (string, data): Data e hora em que o jogo foi iniciado, no formato ISO 8601.

#### Exemplo de Resposta:
```json
{
    "id": 1,
    "numberOfPlayers": 4,
    "cardsPerPlayer": 7,
    "deckId": "gkv50xv60ukp",
    "winner": "Jogador 1",
    "highestScore": 68,
    "gameTimestamp": "2024-11-19T11:25:42.316468"
}
```

### GET /game/history
Consulta o histórico de todos os jogos realizados.

#### Parâmetros de Entrada:
- Nenhum.

### Exemplo de Requisição:
```markdown
GET http://localhost:8080/game/history/
```

#### Parâmetros de Saída:
- `games` (array de objetos): Lista do histórico dos jogos.
    - Cada objeto contém:
        - `id` (inteiro): ID único do jogo.
        - `numberOfPlayers` (inteiro): Número de jogadores no jogo.
        - `cardsPerPlayer` (inteiro): Número de cartas distribuídas para cada jogador.
        - `deckId` (string): ID do baralho utilizado.
        - `winner` (string): Nome do vencedor do jogo.
        - `highestScore` (inteiro): Maior pontuação alcançada no jogo.
        - `gameTimestamp` (string, data): Data e hora em que o jogo foi iniciado, no formato ISO 8601.

#### Exemplo de Resposta:
```json
[
    {
        "id": 1,
        "numberOfPlayers": 4,
        "cardsPerPlayer": 7,
        "deckId": "gkv50xv60ukp",
        "winner": "Jogador 1",
        "highestScore": 68,
        "gameTimestamp": "2024-11-19T11:25:42.316468"
    },
    {
        "id": 2,
        "numberOfPlayers": 6,
        "cardsPerPlayer": 5,
        "deckId": "mmtepja8n2n1",
        "winner": "Jogador 4",
        "highestScore": 40,
        "gameTimestamp": "2024-11-19T11:35:50.242828"
    }
]
```

## Arquitetura

O projeto segue o padrão **Arquitetura Hexagonal**, também conhecido como **Arquitetura Limpa**. Esse padrão busca separar as responsabilidades da aplicação de forma que o núcleo da lógica de negócios não dependa de frameworks, bancos de dados ou outras infraestruturas.

### Componentes da Arquitetura:
1. **Camada de Domínio**:
    - Contém as regras de negócio principais, como as lógicas para a execução de um jogo e as validações de entrada.
    - Não depende de nada além de interfaces e entidades simples.

2. **Camada de Aplicação**:
    - Responsável pela comunicação entre a camada de domínio e os adaptadores externos (como banco de dados, serviços externos, etc.).
    - Utiliza interfaces para interagir com a camada de domínio.

3. **Adaptadores (Camada Externa)**:
    - Adaptadores que conectam a aplicação ao mundo externo, como o banco de dados, a API (controladores REST), etc.

### Como a Arquitetura Hexagonal Se Aplica ao Código:
A comunicação entre a camada de domínio e os serviços externos é feita por meio de **interfaces**. Por exemplo:
- O controlador REST chama a camada de serviço, que por sua vez, interage com a camada de domínio.
- O banco de dados é acessado através de **repositories** que são implementados por adaptadores (ex: JDBC, JPA).

Isso garante que a lógica de negócios possa ser testada e desenvolvida de forma independente de qualquer framework ou banco de dados.

**Desenvolvido por**: Natalia Ferreira D'Angelo
