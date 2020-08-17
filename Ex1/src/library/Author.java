//this class enables the users to set an author and a set of methods to receive and display his information with requested format
package library;
public class Author {
	private String name;
	private  int birthYear;
	public Author(String name,int birthYear) { //initialize author
		this.name=name;
		this.birthYear=birthYear;
	}
	public String getName() {
		return name;
	}
	public int getBirthYear() {
		return birthYear;
	}
	public int getAge(int thisYear) {
		return (thisYear-birthYear);
	}
	public String toString() {
		return name+"("+birthYear+")";
	}
}
