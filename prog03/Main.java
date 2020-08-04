package prog03;

import prog02.GUI;
import prog02.UserInterface;

public class Main {
	  /** Use this variable to store the result of each call to fib. */
	  public static double fibn;

	  /** Determine the average time in microseconds it takes to calculate
	      the n'th Fibonacci number.
	      @param fib an object that implements the Fib interface
	      @param n the index of the Fibonacci number to calculate
	      @param ncalls the number of calls to average over
	      @return the average time per call
	  */
	  public static double averageTime (Fib fib, int n, int ncalls) {
	    // Get the current time in nanoseconds.
	    long start = System.nanoTime();

	    // Call fib(n) ncalls times (needs a loop!).
	    for(int i = 0; i < ncalls; i++) 
	    	fibn = fib.fib(n);

	    // Get the current time in nanoseconds.
	    long end = System.nanoTime();

	    // Return the average time converted to microseconds averaged over ncalls.
	    return (end - start) / 1000.0 / ncalls;
	  }

	  /** Determine the time in microseconds it takes to to calculate the
	      n'th Fibonacci number.  Average over enough calls for a total
	      time of at least one second.
	      @param fib an object that implements the Fib interface
	      @param n the index of the Fibonacci number to calculate
	      @return the time it it takes to compute the n'th Fibonacci number
	  */
	  public static double accurateTime (Fib fib, int n) {
	    // Get the time in microseconds using the time method above.
	    double t = averageTime(fib, n, 1);

	    // If the time is (equivalent to) more than a second, return it.
	    if (t > 1000000)
	    	return t; 

	    // Estimate the number of calls that would add up to one second.
	    // Use   (int)(YOUR EXPESSION)   so you can save it into an int variable.
	    int numcalls = (int) (1000000/t);


	    // Get the average time using averageTime above and that many
	    // calls and return it.
	    return averageTime(fib, n, numcalls);
	  }

	  private static UserInterface ui = new GUI("Fibonacci experiments");

	  public static void doExperiments (Fib fib) {
	    System.out.println("doExperiments " + fib);
	    String[] commands = {
					"Fibonacci Experiment",
					"Cancel"
				};
	    while(true) {
	    	int c = ui.getCommand(commands);
	    	double constant = 0;
	    	switch (c) {
	    		case -1:
	    			ui.sendMessage("You shut down the program, restarting.  Use Exit to exit.");
	    			break;
	    		case 0:
	    			String numStr = ui.getInfo(" Enter the Number");
	    			if (numStr != null && !numStr.isEmpty() && numStr.matches("-?\\d+(\\.\\d+)?")) {
	    				int number = Integer.parseInt(numStr);
	    				if (number < 0) {
	    					double estimateTime = 0;
	    					double time = accurateTime(fib, number);
	    					if (constant == 0)
	    						constant = time / fib.O(number);
	    					else {
	    						estimateTime = c * fib.O(number);
	    						ui.sendMessage("Estimated Time: " + estimateTime);
	    					}
	    					double errorPercentage = Math.abs(estimateTime - time) / 100; 
	    					ui.sendMessage("Entered number is: " + number + " fib(n): " + fibn + " time took to finish: " + time +" and Error Percentage: " + errorPercentage);
	    				} else {
	    					ui.sendMessage("Please enter a Position Integer");
	    				}
	    			} else {
	    				ui.sendMessage("Please enter a valid number");
	    			}
	    			break;
	    		case 1:
	    			ui.sendMessage("Good Bye!");
	    			return;
	    	}
	    }
	  }

	  public static void doExperiments () {
		  Fib fib;
		  String[] commands = {
				  				"Constant Fib",
				  				"Exponential Fib",
				  				"Linear Fib",
				  				"Log Fib",
				  				"Power Fib",
				  				"Mystery Fib",
				  				"Exit"
		  					};
		  while(true) {
			  int c = ui.getCommand(commands);
			  switch(c) {
			  		case -1: 
			  			ui.sendMessage("You shut down the program, restarting.  Use Exit to exit.");
			  			break;
			  		case 0:
			  			// Constant Fib
			  			fib = new ConstantFib();
			  			doExperiments(fib);
			  			break;
			  		case 1:
			  			// Exponential Fib
			  			fib = new ExponentialFib();
			  			doExperiments(fib);
			  			break;
			  		case 2: 
			  			// Linear Fib
			  			fib = new LinearFib();
			  			doExperiments(fib);
			  			break;
			  		case 3: 
			  			// Log Fib
			  			fib = new LogFib();
			  			doExperiments(fib);
			  			break;
			  		case 4:
			  			// Power Fib
			  			fib = new PowerFib();
			  			doExperiments(fib);
			  			break;
			  		case 5: 
			  			// Mystery Fib
			  			fib = new MysteryFib();
			  			doExperiments(fib);
			  			break;
			  		case 6:
			  			ui.sendMessage("Goodbye!!");
			  			return;
			  }
		  }
	  }

	  static void labExperiments () {
	    // Create (Exponential time) Fib object and test it.
	    Fib efib = new ExponentialFib();
	    System.out.println(efib);
	    for (int i = 0; i < 11; i++)
	      System.out.println(i + " " + efib.fib(i));
	    
	    // Determine running time for n1 = 20 and print it out.
	    int n1 = 20;
	    double time1 = averageTime(efib, n1, 1000);
	    System.out.println("n1 " + n1 + " time1 " + time1);
	    int numcallsForN1 = (int)(1000000/time1);
	    System.out.println("Number of calls (trials) for n1 in 1 second : " + numcallsForN1);
	    time1 = averageTime(efib, n1, numcallsForN1); 
	    System.out.println("n1 " + n1 + " modified time1 " + time1);
	    time1 = accurateTime(efib, n1 ); 
	    System.out.println("n1: " + n1 + " time1 using accurateTime " + time1);
	    
	    // Calculate constant:  time = constant times O(n).
	    double c = time1 / efib.O(n1);
	    System.out.println("c " + c);
	    
	    // Estimate running time for n2=30.
	    int n2 = 30;
	    double time2est = c * efib.O(n2);
	    System.out.println("n2 " + n2 + " estimated time " + time2est);
	    int numcallsForN2 = (int)((1000000)/time2est);
	    System.out.println("Number of calls (trials) for n2 in 1 second: " + numcallsForN2);
	    time2est = averageTime(efib, n2, numcallsForN2); 
	    System.out.println("n2 " + n2 + " modified time " + time2est);
	    time2est = accurateTime(efib, n2); 
	    System.out.println("n2: " + n2 + " Time2est using accurateTime " + time2est);
	    
	    // Calculate actual running time for n2=30.
	    double time2 = averageTime(efib, n2, 100);
	    System.out.println("n2 " + n2 + " actual time " + time2);
	    
	    // estimate time for fib(100)
	    int n3 = 100;
	    double time3est = c * efib.O(n3);
	    System.out.println("n3 " + n3 + " estimated time " + time3est);
	    
	    System.out.println();
	    
	    // Linear Fib
	    Fib linearFib = new LinearFib();
	    System.out.println(linearFib);
	    for (int i = 0; i < 11; i++)
	        System.out.println(i + " " + linearFib.fib(i));
	    
	    time1 = averageTime(linearFib, n1, 1000);
	    System.out.println("n1 " + n1 + " time1 " + time1);
	    numcallsForN1 = (int)(1000000/time1);
	    System.out.println("Number of calls (trials) for n1 in 1 second : " + numcallsForN1);
	    time1 = averageTime(linearFib, n1, numcallsForN1); 
	    System.out.println("n1 " + n1 + " modified time1 " + time1);
	    time1 = accurateTime(linearFib, n1 ); 
	    System.out.println("n1: " + n1 + " time1 using accurateTime " + time1);
	    
	    c = time1 / linearFib.O(n1);
	    System.out.println("c " + c);
	   
	    n2 = 30;
	    time2est = c * linearFib.O(n2);
	    System.out.println("n2 " + n2 + " estimated time " + time2est);
	    numcallsForN2 = (int)((1000000)/time2est);
	    System.out.println("Number of calls (trials) for n2 in 1 second: " + numcallsForN2);
	    time2est = averageTime(linearFib, n2, numcallsForN2); 
	    System.out.println("n2 " + n2 + " modified time " + time2est);
	    time2est = accurateTime(linearFib, n2); 
	    System.out.println("n2: " + n2 + " Time2est using accurateTime " + time2est);
	    
	    time2 = averageTime(linearFib, n2, 100);
	    System.out.println("n2 " + n2 + " actual time " + time2);
	    
	    n3 = 100;
	    time3est = c * linearFib.O(n3);
	    System.out.println("n3 " + n3 + " estimated time " + time3est);
	    
	    System.out.println();
	    
	    // Log Fib
	    Fib logFib = new LogFib();
	    System.out.println(logFib);
	    for (int i = 0; i < 11; i++)
	        System.out.println(i + " " + logFib.fib(i));
	    
	    time1 = averageTime(logFib, n1, 1000);
	    System.out.println("n1 " + n1 + " time1 " + time1);
	    numcallsForN1 = (int)(1000000/time1);
	    System.out.println("Number of calls (trials) for n1 in 1 second : " + numcallsForN1);
	    time1 = averageTime(logFib, n1, numcallsForN1); 
	    System.out.println("n1 " + n1 + " modified time1 " + time1);
	    time1 = accurateTime(logFib, n1 ); 
	    System.out.println("n1: " + n1 + " time1 using accurateTime " + time1);
	    
	    c = time1 / logFib.O(n1);
	    System.out.println("c " + c);
	   
	    n2 = 30;
	    time2est = c * logFib.O(n2);
	    System.out.println("n2 " + n2 + " estimated time " + time2est);
	    numcallsForN2 = (int)((1000000)/time2est);
	    System.out.println("Number of calls (trials) for n2 in 1 second: " + numcallsForN2);
	    time2est = averageTime(logFib, n2, numcallsForN2); 
	    System.out.println("n2 " + n2 + " modified time " + time2est);
	    time2est = accurateTime(logFib, n2); 
	    System.out.println("n2: " + n2 + " Time2est using accurateTime " + time2est);
	    
	    time2 = averageTime(logFib, n2, 100);
	    System.out.println("n2 " + n2 + " actual time " + time2);
	    
	    n3 = 100;
	    time3est = c * logFib.O(n3);
	    System.out.println("n3 " + n3 + " estimated time " + time3est);
	    
	    System.out.println();
	    
	    // Constant Fib
	    Fib constantFib = new ConstantFib();
	    System.out.println(constantFib);
	    for (int i = 0; i < 11; i++)
	        System.out.println(i + " " + constantFib.fib(i));
	    
	    time1 = averageTime(constantFib, n1, 1000);
	    System.out.println("n1 " + n1 + " time1 " + time1);
	    numcallsForN1 = (int)(1000000/time1);
	    System.out.println("Number of calls (trials) for n1 in 1 second : " + numcallsForN1);
	    time1 = averageTime(constantFib, n1, numcallsForN1); 
	    System.out.println("n1 " + n1 + " modified time1 " + time1);
	    time1 = accurateTime(constantFib, n1 ); 
	    System.out.println("n1: " + n1 + " time1 using accurateTime " + time1);
	    
	    c = time1 / constantFib.O(n1);
	    System.out.println("c " + c);
	   
	    n2 = 30;
	    time2est = c * constantFib.O(n2);
	    System.out.println("n2 " + n2 + " estimated time " + time2est);
	    numcallsForN2 = (int)((1000000)/time2est);
	    System.out.println("Number of calls (trials) for n2 in 1 second: " + numcallsForN2);
	    time2est = averageTime(constantFib, n2, numcallsForN2); 
	    System.out.println("n2 " + n2 + " modified time " + time2est);
	    time2est = accurateTime(constantFib, n2); 
	    System.out.println("n2: " + n2 + " Time2est using accurateTime " + time2est);
	    
	    time2 = averageTime(constantFib, n2, 100);
	    System.out.println("n2 " + n2 + " actual time " + time2);
	    
	    n3 = 100;
	    time3est = c * constantFib.O(n3);
	    System.out.println("n3 " + n3 + " estimated time " + time3est);
	    
	    System.out.println();
	  }

	  /**
	   * @param args the command line arguments
	   */
	  public static void main (String[] args) {
		  //labExperiments();
		  //doExperiments(new ExponentialFib());
		  doExperiments();
	  }
	}

	