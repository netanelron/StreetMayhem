//this class enables user to set cards and methods to receive information about the card,display it and compare two cards
package cards;
public class Card {
	private int num;
	private int suit;
	public Card (int num,int suit) {
		this.num=num;
		this.suit=suit;
	}
	public int getNum() {
		return num;
	}
	public int getSuit() {
		return suit;
	}
	public String toString() { //display card in the format as requested
		if(suit==0)
			return num+"C";
		else if(suit==1)
			return num+"D";
		else if(suit==2)
			return num+"H";
		else if(suit==3)
			return num+"S";
		return ""; //default view for unrecognized pattern of a card
	}
	public int compareTo(Card other) { //compares two cards
		if(this.num==other.num)	//if the num is equal we need to compare the suit
			return this.suit-other.suit;
		return this.num-other.num;	//num wasn't equal so compare it
		}
	}
