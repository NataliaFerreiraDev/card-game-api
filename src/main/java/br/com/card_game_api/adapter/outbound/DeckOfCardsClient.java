package br.com.card_game_api.adapter.outbound;

import br.com.card_game_api.adapter.outbound.dto.DeckDTO;
import br.com.card_game_api.dto.CardDTO;
import br.com.card_game_api.exception.DeckCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component
public class DeckOfCardsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeckOfCardsClient.class);

    private final RestTemplate restTemplate;

    @Value("${deckofcards.api.baseurl}")
    private String apiBaseUrl;

    public DeckOfCardsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Cria um novo deck com a quantidade especificada.
     *
     * @param numberOfDecks O número de decks a serem criados.
     * @return O deckId do deck criado.
     * @throws DeckCreationException Se a criação do deck falhar.
     */
    public String createDeck(int numberOfDecks) throws DeckCreationException {
        if (numberOfDecks <= 0) {
            throw new IllegalArgumentException("O número de decks deve ser maior que zero.");
        }

        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + "new/shuffle/")
                .queryParam("deck_count", numberOfDecks)
                .toUriString();

        try {
            LOGGER.info("Criando deck com {} deck(s) usando a API externa.", numberOfDecks);

            DeckDTO deckDTO = restTemplate.getForObject(url, DeckDTO.class);

            if (deckDTO == null || deckDTO.getDeckId() == null) {
                throw new DeckCreationException("Erro ao criar o deck. Não foi possível recuperar o deckId.");
            }

            LOGGER.info("Deck criado com sucesso: {}", deckDTO.getDeckId());
            return deckDTO.getDeckId();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new DeckCreationException("Falha ao se comunicar com a API externa.", e);

        } catch (Exception e) {
            throw new DeckCreationException("Erro inesperado ao tentar criar o deck.", e);
        }
    }

    /**
     * Distribui as cartas do deck.
     *
     * @param deckId O ID do deck a ser utilizado.
     * @param numberOfCards O número de cartas a serem distribuídas.
     * @return A lista de cartas distribuídas.
     * @throws DeckCreationException Se a distribuição das cartas falhar.
     */
    public List<CardDTO> dealCards(String deckId, int numberOfCards) throws DeckCreationException {
        if (deckId == null || deckId.isEmpty()) {
            throw new IllegalArgumentException("O deckId não pode ser nulo ou vazio.");
        }

        if (numberOfCards <= 0) {
            throw new IllegalArgumentException("O número de cartas deve ser maior que zero.");
        }

        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + "{deck_id}/draw/")
                .queryParam("count", numberOfCards)
                .buildAndExpand(deckId)
                .toUriString();

        try {
            LOGGER.info("Distribuindo {} carta(s) do deck com ID: {}", numberOfCards, deckId);

            DeckDTO deckDTO = restTemplate.getForObject(url, DeckDTO.class);

            if (deckDTO == null || deckDTO.getCards() == null || deckDTO.getCards().isEmpty()) {
                throw new DeckCreationException("Erro ao distribuir as cartas. Resposta inválida ou sem cartas.");
            }

            LOGGER.info("{} carta(s) distribuída(s) com sucesso do deck com ID: {}", numberOfCards, deckId);
            return deckDTO.getCards();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new DeckCreationException("Erro ao se comunicar com a API externa ao distribuir as cartas.", e);

        } catch (Exception e) {
            throw new DeckCreationException("Erro inesperado ao tentar distribuir as cartas.", e);
        }
    }

}
