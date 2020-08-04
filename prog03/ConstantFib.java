package prog03;

public class ConstantFib implements Fib {
	/** The Fibonacci number generator 0, 1, 1, 2, 3, 5, ...
	@param n index
	@return nth Fibonacci number
    */
    public double fib (int n) {
    	return (Math.pow(g1, n) - Math.pow(g2, n)) / sqrt5;
    }

    /** The order O() of the implementation.
	@param n index
	@return the function of n inside the O()
    */
    public double O (int n) {
    	return 1;
    }

    protected double sqrt5 = Math.sqrt(5);
    protected double g1 = (1 + sqrt5) / 2;
    protected double g2 = (1 - sqrt5) / 2;
}
