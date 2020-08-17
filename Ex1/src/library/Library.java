//this class enables the user to set a library,and methods to set books in the library and view books in the library
package library;
public class Library {
	private int size;
	private Book[] arr;
	public Library(int size) {
		this.size=size;
		arr=new Book[size];
	}
	public void setBook(int bookNum,String title,Author auth) {
		if(bookNum>=size)	//this was not requested in the task,but is here to remove the "unused" warning
			System.out.println("Error-invalid book number");
		else
			arr[bookNum]=new Book(title,auth);
	}
	public Book getBook(int bookNum) {
		if(bookNum>=size) {	//this was not requested in the task,but is here to remove the "unused" warning
			System.out.println("Error-invalid book number");
			return null;
		}
		return arr[bookNum];
	}
}
