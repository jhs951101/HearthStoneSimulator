package pkg;

public class Card {
	private int cost;
	private int hp;
	private int atk;
	
	private int fieldTurn;
	private float numOfAttack;
	private String func;
	
	public Card() {
		fieldTurn = -1;
		cost = 1;
		hp = 1;
		atk = 1;
		func = "";
	}
	
	public Card(int c, int h, int a, String f) {
		fieldTurn = -1;
		cost = c;
		hp = h;
		atk = a;
		func = f;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public int getFieldTurn() {
		return fieldTurn;
	}

	public void setFieldTurn(int fieldTurn) {
		this.fieldTurn = fieldTurn;
	}
	
	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public float getNumOfAttack() {
		return numOfAttack;
	}

	public void addNumOfAttack() {
		if(func.equals("ÁúÇ³"))
			this.numOfAttack += 0.5f;
		else
			this.numOfAttack += 1.0f;
	}
	
	public void initializeNumOfAttack() {
		this.numOfAttack = 0;
	}
}
