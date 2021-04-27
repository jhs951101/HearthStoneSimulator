package pkg;

import java.util.ArrayList;
import java.util.Random;

public class Player {
	private ArrayList<Card> cardsInHand;
	private ArrayList<Card> cardsInField;
	
	private Card cardsALL[];
	private short size_cardsALL;
	
	private int playerHP;
	private int playerEnergy;
	private int currentPlayerEnergy;
	private int panaltySize;
	
	public Player() {
		cardsInField = new ArrayList<Card>();
		cardsInHand = new ArrayList<Card>();
		cardsALL = new Card[30];
		
		initialize();
	};
	
	private Card getRandomCard() {
		Card card = null;
		int rn = GameController.randomNumber(1,8);
		
		if(rn == 1)
			card = new Card(2, 2, 2, "");
		else if(rn == 2)
			card = new Card(4, 5, 4, "도발");
		else if(rn == 3)
			card = new Card(2, 2, 2, "독성");
		else if(rn == 4)
			card = new Card(3, 1, 3, "돌진");
		else if(rn == 5)
			card = new Card(2, 2, 2, "속공");
		else if(rn == 6)
			card = new Card(2, 1, 2, "은신");
		else if(rn == 7)
			card = new Card(1, 2, 1, "질풍");
		else if(rn == 8)
			card = new Card(2, 2, 2, "흡수");
		
		return card;
	}
	
	public void initialize() {
		cardsInField.clear();
		cardsInHand.clear();
		size_cardsALL = 0;
		playerHP = 30;
		playerEnergy = 0;
		panaltySize = 1;
		
		while(size_cardsALL < cardsALL.length) {
			cardsALL[size_cardsALL] = getRandomCard();
			size_cardsALL++;
		}
	}
	
	public void cardToHand() {
		if(size_cardsALL > 0) {
			if(size_cardsALL == 1)
				System.out.println("Warning: There is only 1 card in your pocket. You will be attacked starting next turn.");
			
			size_cardsALL--;
			
			if(cardsInHand.size() < 10)
				cardsInHand.add( cardsALL[size_cardsALL] );
			else
				System.out.println("Warning: The cards in your hand is too much.");
		}
		else {
			System.out.println("Warning: You have no card in your pocket. You were attacked by " + panaltySize);
			playerHP -= panaltySize;
			panaltySize++;
		}
	}
	
	public void cardToField(int i, int currentTurn) {
		if(cardsInHand.size() > 0) {
			Card c = cardsInHand.get(i);
			c.setFieldTurn(currentTurn);
			currentPlayerEnergy -= c.getCost();
			
			cardsInField.add(c);
			cardsInHand.remove(i);
		}
	}

	public Card getCardsInHand(int i) {
		return cardsInHand.get(i);
	}
	
	public Card getCardsInField(int i) {
		return cardsInField.get(i);
	}
	
	public int getSize_CardsInHand() {
		return cardsInHand.size();
	}
	
	public int getSize_CardsInField() {
		return cardsInField.size();
	}
	
	public int getPlayerHP() {
		return playerHP;
	}
	
	public void healPlayerHP(int hp) {
		for(int i=1; i<=hp; i++) {
			if(playerHP < 30)
				playerHP++;
			else
				break;
		}
	}
	
	public void initializePlayerEnergy_NextTurn() {
		if(playerEnergy < 10)
			playerEnergy += 1;
		
		currentPlayerEnergy = playerEnergy;
	}
	
	public void initializeNumOfAttack() {
		for(int i=0; i<cardsInField.size(); i++)
			cardsInField.get(i).initializeNumOfAttack();
	}
	
	public int getCurrentPlayerEnergy() {
		return currentPlayerEnergy;
	}
	
	public void setCurrentPlayerCost(int currentPlayerCost) {
		this.currentPlayerEnergy = currentPlayerCost;
	}
	
	public void printCardsInHand() {
		for(int i=0; i<cardsInHand.size(); i++) {
			Card c = cardsInHand.get(i);
			System.out.println(i+1 + " : (" + c.getCost() + ") HP:" + c.getHp() + " ATK:" + c.getAtk() + " " + c.getFunc());
		}
	}
	
	public void printCardsInField() {
		for(int i=0; i<cardsInField.size(); i++) {
			Card c = cardsInField.get(i);
			System.out.println(i+1 + " : (" + c.getCost() + ") HP:" + c.getHp() + " ATK:" + c.getAtk() + " " + c.getFunc());
		}
	}
	
	public void giveDamageToFighter(int cardIndex, int atk) {
		Card c = cardsInField.get(cardIndex);
		int newHP = c.getHp() - atk;
		
		if(newHP > 0)
			c.setHp(newHP);
		else
			destroyFighter(cardIndex);
	}
	
	public void destroyFighter(int i) {
		cardsInField.remove(i);
	}

	public void giveDamageToPlayer(int atk) {
		playerHP -= atk;
	}
}
