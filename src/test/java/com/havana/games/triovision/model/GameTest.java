package com.havana.games.triovision.model;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class GameTest {

	private Game game;

	@Before
	public void setup() {
		game = new Game(2);
	}

	@Test
	public void testGameIsIntantiableWithTwoPlayers() {
		new Game(2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGameIsNotInstantiableWithOnePlayer() {
		new Game(1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGameIsNotInstantiableWithNegativePlayers() {
		new Game(-1);
	}

	@Test
	public void testGameReturnsPlayerByIndex() {
		Player p = game.getPlayer(1);

		assertThat(p, is(notNullValue()));
	}

	@Test
	public void testGameReturnsSamePlayerForSameIndex() {
		Player p1 = game.getPlayer(1);
		Player p2 = game.getPlayer(1);

		assertThat(p1, is(sameInstance(p2)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGameThrowsExceptionForInvalidIndexForPlayer() {
		game.getPlayer(3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGameThrowsExceptionForNegativeIndexForPlayer() {
		game.getPlayer(-1);
	}

	@Test
	public void testEveryGameHasANonNullBoard() {
		Board b = game.getBoard();

		assertThat(b, is(notNullValue()));
	}

	@Test
	public void testGameMaintainsASingleBoard() {
		Board b1 = game.getBoard();
		Board b2 = game.getBoard();

		assertThat(b1, is(sameInstance(b2)));
	}

	@Test
	public void testGameHasNonNullDeck() {
		Deck d = game.getDeck();

		assertThat(d, is(notNullValue()));
	}

	@Test
	public void testGameMaintainsSingleDeck() {
		Deck d1 = game.getDeck();
		Deck d2 = game.getDeck();

		assertThat(d1, is(sameInstance(d2)));
	}

	@Test
	public void testOpenCardsAreZeroBeforeStart() {
		List<Card> openCards = game.getOpenCards();

		assertThat(openCards.size(), is(0));
	}

	@Test
	public void testThereAreTwelveOpenCardsAfterStart() {
		game.start();

		List<Card> openCards = game.getOpenCards();

		assertThat(openCards.size(), is(12));
	}

	@Test
	public void testCallingStartTwiceHasNoEffect() {
		game.start();
		game.start();

		List<Card> openCards = game.getOpenCards();

		assertThat(openCards.size(), is(12));
	}

	@Test
	public void testMakeCorrectMoveOnBehalfOfPlayer() {
		Board board = game.getBoard();
		Card card = Card.builder()
				.topRight(Pawn.BLUE)
				.middleRight(Pawn.YELLOW)
				.bottomLeft(Pawn.YELLOW)
				.build();
		
		// Moving the top left yellow pawn one square to the right makes the card match
		Board moveMadeBoard = board.swap(1, 0, 1, 1);
		
		boolean moveMade = game.makeMoveForPlayer(1, card, moveMadeBoard);
		
		assertThat(moveMade, is(true));
	}
	
	@Test
	public void testMakeInvalidMoveOnBehalfOfPlayer() {
		Board board = game.getBoard();
		Card card = Card.builder()
				.topRight(Pawn.BLUE)
				.middleRight(Pawn.YELLOW)
				.bottomLeft(Pawn.YELLOW)
				.build();
		
		// Moving the top left blue pawn one square below does not make the card match
		Board moveMadeBoard = board.swap(0, 1, 1, 1);
		
		boolean moveMade = game.makeMoveForPlayer(1, card, moveMadeBoard);
		
		assertThat(moveMade, is(false));
	}
	
	@Test
	public void testCorrectMoveOnBehalfOfPlayerAddsCardToPlayerPile() {
		Board board = game.getBoard();
		Card card = Card.builder()
				.topRight(Pawn.BLUE)
				.middleRight(Pawn.YELLOW)
				.bottomLeft(Pawn.YELLOW)
				.build();
		
		// Moving the top left yellow pawn one square to the right makes the card match
		Board moveMadeBoard = board.swap(1, 0, 1, 1);
		
		int playerIndex = 1;
		Player player = game.getPlayer(playerIndex);
		
		assertThat(player.getWonCards().size(), is(0));
		
		boolean moveMade = game.makeMoveForPlayer(playerIndex, card, moveMadeBoard);
		
		assertThat(moveMade, is(true));
		assertThat(player.getWonCards().size(), is(1));
	}

	@Test
	public void testCorrectMoveOnBehalfOfPlayerRemovesCardFromOpenCards() {
		Board board = game.getBoard();
		Card card = Card.builder()
				.topRight(Pawn.BLUE)
				.middleRight(Pawn.YELLOW)
				.bottomLeft(Pawn.YELLOW)
				.build();
		
		// Moving the top left yellow pawn one square to the right makes the card match
		Board moveMadeBoard = board.swap(1, 0, 1, 1);
		
		boolean moveMade = game.makeMoveForPlayer(1, card, moveMadeBoard);
		
		assertThat(moveMade, is(true));
		assertFalse(game.getOpenCards().contains(card));
	}
	
	@Test
	@Ignore
	public void testInvalidMoveOnBehalfOfPlayerRetainsCardInOpenCards() {
		Board board = game.getBoard();
		Card card = Card.builder()
				.topRight(Pawn.BLUE)
				.middleRight(Pawn.YELLOW)
				.bottomLeft(Pawn.YELLOW)
				.build();
		
		// Moving the top left blue pawn one square below does not make the card match
		Board moveMadeBoard = board.swap(0, 1, 1, 1);
		
		boolean moveMade = game.makeMoveForPlayer(1, card, moveMadeBoard);
		
		assertThat(moveMade, is(false));
		assertTrue(game.getOpenCards().contains(card));
	}
}
