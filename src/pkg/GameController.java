package pkg;

import java.util.Random;
import java.util.Scanner;

public class GameController {
	private static Random rand;
	
	private Player[] player;
	private Card enemyFighter;
	private Card myFighter;
	private int enemyFighterIndex;
	private int myFighterIndex;
	
	private String input;
	private int currentPlayerTurn;
	private int enemyPlayerTurn;
	private float currentTurn;
	
	public GameController() {
		rand = new Random();
		player = new Player[2];
		
		for(int i=0; i<2; i++)
			player[i] = new Player();
	}
	
	public static int randomNumber(int min, int max) {
		return rand.nextInt(max-min+1) + min;
	}
	
	public void GAME() {
		System.out.println("Welcome to the HearthStone Simulator!");
		
		while(true) {
			System.out.print("Would you like to play the game? (y/n): ");
			getInput();
			clearScreen();
			
			if(input.equals("n"))
				break;
			
			initialize();
			
			System.out.println("Player " + (currentPlayerTurn+1) + " is first.\n");
			
			while(true) {
				System.out.println("Player " + (currentPlayerTurn+1) + " Turn (Turn " + (int)currentTurn + ")");
				System.out.println("1 : Send cards to field");
				System.out.println("2 : Attack units or a player");
				System.out.println("3 : Give turn to enemy player");
				System.out.println("-1 : Give up this game");
				System.out.print("Choose what you want to do: ");
				getInput();
				clearScreen();
				
				if(input.equals("1")) {
					sendCardsToField();
				}
				else if(input.equals("2")) {
					attackEnemies();
				}
				else if(input.equals("3")) {
					giveTurnToEnemyPlayer();
				}
				else if(input.equals("-1")) {
					giveUpThisGame();
				}
				
				if(player[currentPlayerTurn].getPlayerHP() <= 0) {
					YouLose();
					break;
				}
				else if(player[enemyPlayerTurn].getPlayerHP() <= 0) {
					YouWin();
					break;
				}
			}
		}
		
		System.out.println("Thank you for playing the game!");
	}
	
	private void initialize() {
		for(int i=0; i<2; i++)
			player[i].initialize();
		
		currentTurn = 1.0f;
		currentPlayerTurn = GameController.randomNumber(0, 1);
		enemyPlayerTurn = 1 - currentPlayerTurn;
		
		player[currentPlayerTurn].initializePlayerEnergy_NextTurn();
		player[currentPlayerTurn].initializeNumOfAttack();
		
		player[currentPlayerTurn].cardToHand();
		player[currentPlayerTurn].cardToHand();
		player[currentPlayerTurn].cardToHand();
		player[enemyPlayerTurn].cardToHand();
		player[enemyPlayerTurn].cardToHand();
		player[enemyPlayerTurn].cardToHand();
	}
	
	private void clearScreen() {   
		System.out.print("\n\n\n\n\n\n\n");
	}
	
	private void getInput() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		input = scanner.next();
	}
	
	private void sendCardsToField() {
		if(player[currentPlayerTurn].getSize_CardsInHand() > 0) {
			
			if(player[currentPlayerTurn].getSize_CardsInField() < 7) {
				while(true) {
					player[currentPlayerTurn].printCardsInHand();
					System.out.println("Player Cost: " + player[currentPlayerTurn].getCurrentPlayerEnergy());
					System.out.print("Choose a card to send to field (Input -1 to cancel): ");
					getInput();
					clearScreen();
					
					if(input.equals("-1"))
						break;
					
					int cost = player[currentPlayerTurn].getCardsInHand( Integer.parseInt(input)-1 ).getCost();
					
					if(cost <= player[currentPlayerTurn].getCurrentPlayerEnergy()) {
						player[currentPlayerTurn].cardToField(Integer.parseInt(input)-1, (int)currentTurn);
					}
					else {
						System.out.println("Error: Not enough cost\n");
					}
				}
			}
			else {
				System.out.println("Error: Your field is full of 7 fighters\n");
			}
		}
		else {
			System.out.println("Error: You have no card in your hand\n");
		}
	}
	
	private void attackEnemies() {
		if(player[currentPlayerTurn].getSize_CardsInField() > 0) {
			while(true) {
				player[currentPlayerTurn].printCardsInField();
				System.out.print("Choose a fighter to use to attack an enemy (Input -1 to cancel): ");
				getInput();
				clearScreen();
				
				if(input.equals("-1"))
					break;
				
				myFighterIndex = Integer.parseInt(input);
				myFighter = player[currentPlayerTurn].getCardsInField(myFighterIndex-1);
				
				if( (int)currentTurn > myFighter.getFieldTurn()
						|| myFighter.getFunc().equals("속공")  || myFighter.getFunc().equals("돌진") ) {
					if(myFighter.getNumOfAttack() < 1) {
						System.out.println("Enemy Player HP: " + player[enemyPlayerTurn].getPlayerHP());
						System.out.println("0 : Enemy Player");
						player[enemyPlayerTurn].printCardsInField();
						System.out.print("Choose an enemy for your fighter to attack (Input -1 to cancel): ");
						getInput();
						clearScreen();
						
						if(input.equals("-1"))
							break;
						
						if(myFighter.getFunc().equals("흡수"))
							player[currentPlayerTurn].healPlayerHP(myFighter.getAtk());
						
						if(input.equals("0")) {
							
							if((int)currentTurn <= myFighter.getFieldTurn() && myFighter.getFunc().equals("속공")) {
								System.out.println("Error: The fighter can only attack the enemy fighter now\n");
							}
							else {
								if(enemyHasTaunt()) {
									System.out.println("Error: You must destroy fighters with Taunt\n");
								}
								else {
									attackEnemyPlayer();
								}
							}
						}
						else {
							enemyFighterIndex = Integer.parseInt(input);
							enemyFighter = player[enemyPlayerTurn].getCardsInField(enemyFighterIndex-1);
							
							if(enemyHasTaunt()) {
								if(enemyFighter.getFunc().equals("도발")) {
									attackEnemyFighter();
								}
								else {
									System.out.println("Error: You must destroy fighters with Taunt\n");
								}
							}
							else {
								attackEnemyFighter();
							}
						}
					}
					else {
						System.out.println("Error: The fighter you selected already attacked\n");
					}
				}
				else {
					System.out.println("Error: You cannot make the fighter attack enemy right now\n");
				}
			}
		}
		else {
			System.out.println("Error: You have no fighters in your field\n");
		}
	}
	
	private void attackEnemyPlayer() {
		player[enemyPlayerTurn].giveDamageToPlayer(myFighter.getAtk());
		myFighter.addNumOfAttack();
		
		if(myFighter.getFunc().equals("은신"))
			myFighter.setFunc("");
	}
	
	private void attackEnemyFighter() {
		
		if(enemyFighter.getFunc().equals("은신")){
			System.out.println("Error: You cannot attack enemies with Stealth\n");
		}
		else {
			myFighter.addNumOfAttack();
			
			if(myFighter.getFunc().equals("은신"))
				myFighter.setFunc("");
			
			if(myFighter.getFunc().equals("독성")) {
				player[currentPlayerTurn].giveDamageToFighter(myFighterIndex-1, enemyFighter.getAtk());
				player[enemyPlayerTurn].destroyFighter(enemyFighterIndex-1);
			}
			else if(enemyFighter.getFunc().equals("독성")) {
				player[enemyPlayerTurn].giveDamageToFighter(enemyFighterIndex-1, myFighter.getAtk());
				player[currentPlayerTurn].destroyFighter(enemyFighterIndex-1);
			}
			else {
				player[enemyPlayerTurn].giveDamageToFighter(enemyFighterIndex-1, myFighter.getAtk());
				player[currentPlayerTurn].giveDamageToFighter(myFighterIndex-1, enemyFighter.getAtk());
			}
		}
	}
	
	private void giveTurnToEnemyPlayer() {
		int temp = currentPlayerTurn;
		currentPlayerTurn = enemyPlayerTurn;
		enemyPlayerTurn = temp;
		currentTurn += 0.5f;
		
		setNextTurn();
	}
	
	private void giveUpThisGame() {
		System.out.print("Would you like to give up this game truly? (y/n): ");
		getInput();
		clearScreen();
		
		if(input.equals("y")) {
			YouLose();
		}
	}
	
	private void setNextTurn() {
		player[currentPlayerTurn].initializePlayerEnergy_NextTurn();
		player[currentPlayerTurn].initializeNumOfAttack();
		player[currentPlayerTurn].cardToHand();
	}
	
	private boolean enemyHasTaunt() {
		for(int i=0; i<player[enemyPlayerTurn].getSize_CardsInField(); i++) {
			Card c = player[enemyPlayerTurn].getCardsInField(i);
			
			if(c.getFunc().equals("도발"))
				return true;
		}
		
		return false;
	}
	
	private void YouLose() {
		System.out.println("Player " + (currentPlayerTurn+1) + " Lose... " + "Player " + (enemyPlayerTurn+1) + " Win!\n");
	}
	
	private void YouWin() {
		System.out.println("Player " + (currentPlayerTurn+1) + " Win! " + "Player " + (enemyPlayerTurn+1) + " Lose...\n");
	}
}