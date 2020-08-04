package prog03;

public class LinearFib  implements Fib {

	@Override
	public double fib(int n) {
		double a = 0, b = 1, c; 
        if (n == 0) 
            return a; 
        for (int i = 2; i <= n; i++) 
        { 
            c = a + b; 
            a = b; 
            b = c; 
        } 
        return b;
	}

	@Override
	public double O(int n) {
		// what is the complexity for linear Fib 
		
		return n;
	}
	

}
