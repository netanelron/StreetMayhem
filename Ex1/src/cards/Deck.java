//this class enables user to initialize a deck in three different ways (further explanation below),and methods to take a card,display the deck and sort the deck
package cards;
public class Deck {
	private int num;
	private Card[] arr;
	public Deck(int num) { //initialize deck with 4*num cards,a total of 0 to "num-1" card numbers with one of each kind 
		this.num=num*4;
		arr=new Card[num*4];
		for(int i=0;i<num;i++)
			for(int j=0;j<4;j++)
				arr[4*i+j]=new Card(i,j);
	}
	public Deck(Deck from,int num) { //initialize a deck by taking "num" cards from the deck "from",if num is greater than deck's size then just take all the deck
		if(from.num>num)
			this.num=num;
		else	//num was greater than "from" deck's size
			this.num=from.num;
		arr=new Card[this.num];
		for(int i=0;i<this.num;i++) 
			arr[i]=from.takeOne();
	}
	public Deck(Deck first, Deck second) {	//initialize a deck by combining "first" and "second" decks
		num=first.num+second.num;
		arr=new Card[num];
		int min=first.num<second.num?first.num:second.num;	//set min to be the minimal deck's size
		Deck maximalDeck=first.num>second.num?first:second;	//set maximalDeck to be the maximal deck
		int i;
		for(i=0;i<min;i++) {	//take cards from each deck until the minimal deck is empty
			arr[2*i]=first.takeOne();
			arr[2*i+1]=second.takeOne();
		}
		i=0;
		while(maximalDeck.num>0) {	//fill the deck with the rest of the maximal deck
			arr[2*min+i]=maximalDeck.takeOne();
			i++;
		}

	}
	public int getNumCards() {
		return num;
	}
	public Card takeOne() {	//return the last card from the deck and remove it from the deck
		Card temp=null;
		if(num>0) {
			num--;
			temp=arr[num];
			arr[num]=null;	//the card is no longer in the deck,therefore we need to remove the pointer to it
		}
		return temp;
	}
	public String toString() {	//display deck in requested format
		String temp=num>0?arr[0].toString():"";	//assign temp to be the first card in the deck,if there are no cards,assign it to ""
		for(int i=1;i<num;i++)
			temp=temp+", "+arr[i];
		return "["+temp+"]";	
	}
	public void sort() {	//sorts the deck with Selection Sort
		Card temp;
		int maxLocation=0,tempArrSize=num;
		for(int i=0;i<num;i++) {
			maxLocation=0;
			for(int j=1;j<tempArrSize;j++)
				if(arr[j].compareTo(arr[maxLocation])>0)
					maxLocation=j;
			temp=arr[tempArrSize-1];
			arr[tempArrSize-1]=arr[maxLocation];
			arr[maxLocation]=temp;
			tempArrSize--;
		}
	}
}
