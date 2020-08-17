//this class enables the user to set a book with given title and author,and a set of methods to receive information about the book
package library;
public class Book {
	private String title;
	private Author auth;
	public Book(String title,Author auth) {
		this.title=title;
		this.auth=auth;
	}
	public String getTitle() {
		return title;
	}
	public String getAuthorName() {
		return auth.getName();
	}
	public int getAuthorBirthYear() {
		return auth.getBirthYear();
	}
	public String toString() {
		return title+" written by "+auth;
	}
}
