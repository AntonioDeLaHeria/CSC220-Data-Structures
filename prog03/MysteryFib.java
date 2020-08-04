package prog03;

public class MysteryFib extends ExponentialFib {
    private double[] save;

    /** The Fibonacci number generator 0, 1, 1, 2, 3, 5, ...
	@param n index
	@return nth Fibonacci number
    */
    public double fib (int n) {
      if (save == null) {
        save = new double[n+1];
        for (int i = 0; i < save.length; i++)
          save[i] = -1;
        double f = fib(n);
        save = null;
        return f;
      }

      if (save[n] == -1)
        save[n] = super.fib(n);
        
      return save[n];
    }
    
    @Override
    public double O (int n) {
    	double order = 1 + 1 + n + 1 + 1 + super.O(n) + 1 + 1 + 1;
		return order;
    }
}