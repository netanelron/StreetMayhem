//this class enables the user to set a certain precision and calculate the root value of a number with said precision
package root;
public class Rooter {
	private double precision;
	public Rooter(double precision) {
		this.precision=precision;
	}
	
	public void setPrecision(double precision) {
		this.precision=precision;
	}
	
	public double sqrt(double x) {
		double one=x/2;
		double two;
		if (x==0) 
			return 0;
		if (x<0) {
			System.out.println("error-can't sqrt a negative number");
			return 0;
		}
		while(true) {
			two=x/one;
			if(one==two)
				break;
			if(Math.abs(one-two)<precision)
				break;
			one=(one+two)/2;
		}
		return one;
	}


}
