//this class enables user to set a city and methods to connect the city to a given road and find the nearest city to said city
package cities;
public class City {
	private String name;
	private Road[] roads;
	private int numRoads;
	public City(String name) {	//initialize city as requested
		this.name=name;
		roads=new Road[10];
		numRoads=0;
	}
	public void connect(Road r) {	//connect road "r" to this city
		roads[numRoads]=r;
		numRoads++;
	}
	public City nearestCity() {	//find the nearest city to this city
		int min=0;
		if(numRoads==0)
			return null;
		for (int i=1;i<numRoads;i++) {
			if(roads[min].getLength()>roads[i].getLength())	//compare distances and set min to be the index of nearest city
				min=i;
		}
		return roads[min].getCity2()==this?roads[min].getCity1():roads[min].getCity2();
	}
	public String toString() {
		return name;
	}
}
