package prog05;

import prog02.GUI;
import prog02.UserInterface;

public class Tower {
	//static UserInterface ui = new GUI("Towers of Hanoi");
	static TestUI ui = new TestUI();

	static public void main(String[] args) {
		int n = getInt("How many disks?");
		if (n <= 0)
			return;
		Tower tower = new Tower(n);

		String[] commands = { "Human plays.", "Computer plays." };
		int c = ui.getCommand(commands);
		if (c == -1)
			return;
		if (c == 0)
			tower.play();
		else
			tower.solve();
	}

	/**
	 * Get an integer from the user using prompt as the request. Return 0 if user
	 * cancels.
	 */
	static int getInt(String prompt) {
		while (true) {
			String number = ui.getInfo(prompt);
			if (number == null)
				return 0;
			try {
				return Integer.parseInt(number);
			} catch (Exception e) {
				ui.sendMessage(number + " is not a number.  Try again.");
			}
		}
	}

	int nDisks;
	StackInt<Integer>[] pegs = (StackInt<Integer>[]) new ArrayStack[3];

	Tower(int nDisks) {
		this.nDisks = nDisks;
		for (int i = 0; i < pegs.length; i++)
			pegs[i] = new ArrayStack<Integer>();

		// EXERCISE: Initialize game with pile of nDisks disks on peg 'a' (pegs[0]).
		for(int i = 0; i < nDisks; i++) {
			pegs[0].push(nDisks-i);
		}
	}

	void play() {
		String[] moves = { "ab", "ac", "ba", "bc", "ca", "cb" };
		/* EXERCISE: player has not moved all the disks to 'c'. */
		/* Not right. */
		while (!(pegs[0].empty() && pegs[1].empty())) {
			displayPegs();
			int imove = ui.getCommand(moves);
			if (imove == -1)
				return;
			String move = moves[imove];
			int from = move.charAt(0) - 'a';
			int to = move.charAt(1) - 'a';
			if (!pegs[from].empty())
				move(from, to);
			else 
				ui.sendMessage("Cannot move disk from empty stack");
		}

		ui.sendMessage("You win!");
	}
	
	/*private boolean checkFinishFlag() {
		StackInt<Integer> helper = new ArrayStack<Integer>();
		int count = 0;
		while(!pegs[2].empty()) {
			helper.push(pegs[2].pop());
			count++;
		}
		
		while (!helper.empty()) {
			pegs[2].push(helper.pop());
		}
		
		if (count == nDisks)
			return true;
		return false;
	}
*/
	String stackToString(StackInt<Integer> peg) {
		StackInt<Integer> helper = new ArrayStack<Integer>();

		// String to append items to.
		String s = "  ";

		// EXERCISE: append the items in peg to s from bottom to top.
		while (!peg.empty()) {
			helper.push(peg.pop());
		}
		
		while (!helper.empty()) {
			s = s + helper.peek() + "  ";
			peg.push(helper.pop());
		}
		
		return s;
	}

	void displayPegs() {
		String s = "";
		for (int i = 0; i < pegs.length; i++) {
			char abc = (char) ('a' + i);
			s = s + abc + stackToString(pegs[i]);
			if (i < pegs.length - 1)
				s = s + "\n";
		}
		ui.sendMessage(s);
	}

	void move(int from, int to) {
		// EXERCISE: move one disk form pegs[from] to pegs[to].
		// Don't allow illegal moves: send a warning message instead.
		// For example "Cannot place disk 2 on top of disk 1."
		// Use ui.sendMessage() to send messages.
		if (!pegs[from].empty() && !pegs[to].empty()) {
			if (pegs[from].peek() > pegs[to].peek()) {
				ui.sendMessage("Cannot place disk " + pegs[from].peek() + " on top of disk " + pegs[to].peek());
				return;
			}
		}
		pegs[to].push(pegs[from].pop());
	}

	// EXERCISE: create Goal class.
	class Goal {
		// Data.
		int num;
		int fromPeg;
		int toPeg;

		// Constructor.
		public Goal(int numDisk, int from, int to) {
			this.num = numDisk;
			this.fromPeg = from;
			this.toPeg = to;
		}

		public String toString() {
			String[] pegNames = { "a", "b", "c" };
			String s = num + pegNames[fromPeg] + pegNames[toPeg]; 
			return s;
		}
		
		/*private boolean isEasy() {
			if (num == 1)
				return true;
			return false;
		}*/
		
		/*private Goal[] generateGoalArr() {
			
			return null;
		}*/
	}
	
	// EXERCISE: display contents of a stack of goals
	String goalStackToString(StackInt<Goal> peg) {
		StackInt<Goal> helperStack = new ArrayStack<Goal>(); 	
		String s = "";
		
		while(!peg.empty()) {
			s = s + peg.peek() + "\n";
			helperStack.push(peg.pop());
		}
		
		while(!helperStack.empty()) {
			peg.push(helperStack.pop());
		}
		
		return s;
	}

	void solve() {
		// EXERCISE
		StackInt<Goal> goalStack = new ListStack<Goal>(); 
		
		Goal goal = new Goal(4, 0, 2);
		
		goalStack.push(goal);
		while(!goalStack.empty()) {
			displayPegs();
			Goal current = goalStack.peek();
			if (current.num > 1) {
				/*Goal[] goalArray = current.generateGoalArr();
				for (int i = goalArray.length-1; i >= 0; i++) {
					goalStack.push(goalArray[i]);
				}*/
				goal = goalStack.pop();
				goalStack.push(new Goal(goal.num-1, 3-(goal.fromPeg+goal.toPeg), goal.toPeg));
				goalStack.push(new Goal(goal.num-(goal.num-1), goal.fromPeg, goal.toPeg));
				goalStack.push(new Goal(goal.num-1, goal.fromPeg, 3-(goal.toPeg+goal.fromPeg)));								
			} else {
				move(goalStack.peek().fromPeg, goalStack.peek().toPeg);
				goalStack.pop();
			}
		}
		ui.sendMessage("You win!");
	}
}