package prog07;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import prog02.GUI;
import prog02.UserInterface;

public class WordStep {
	static UserInterface ui = new GUI("Word Step");
	static List<String> words = new ArrayList<String>();
	
	public static void main(String[] args) {
		WordStep game = new WordStep();
		String fileName = ui.getInfo("Please enter the file name: ");
		game.loadWords(fileName);
		String start, target;
		while(true) {
			start = ui.getInfo("Enter the starting word:");
			target = ui.getInfo("Enter the target word:");
			if (find(start) != -1 && find(target) != -1)
				break;
			else {
				ui.sendMessage("The start and/or the target words are not in the list");
			}
		}
		String[] commands = { "Humman Plays.", "Computer Plays." };
		int c = ui.getCommand(commands);
		if(c == 0) 
			game.play(start,target);
		else if (c == 1)
			game.solve(start, target);
		
		/*System.out.println(numDifferent("snow", "slot"));*/
	}
	
	void play(String start, String target) {
		String nextWord = "";
		while (true) { 
			ui.sendMessage("Current postition: " +  start);
			ui.sendMessage("Target is: " + target);
			nextWord = ui.getInfo(" What is the next word: "); 
			if (find(nextWord) == -1) {
				ui.sendMessage(nextWord + " is not a word.");
				continue;
			}
			if (offBy1(nextWord, start)) {
				start = nextWord;
			} else {
				ui.sendMessage("Sorry, but " + nextWord + " differs by more than one letter from " + start);
			}
			if (start.equals(target)) {
				ui.sendMessage("You Win!!!");
				return;
			}
		}
	}
	
	void solve(String start, String target) {
		int pollCount = 0;
		int[] parents = new int[words.size()];
		for (int i=0; i<parents.length; i++)
			parents[i] = -1;
		PriorityQueue<Integer> indexQueue = new PriorityQueue<>(new IndexComparator(parents, target));
		//PriorityQueue<Integer> indexQueue = new PriorityQueue<>();
		//Queue<Integer> indexQueue = new LinkedQueue<Integer>();
		//Heap<Integer> indexQueue = new Heap<>(new IndexComparator(parents, target));
		indexQueue.offer(find(start));
		String currentWord;
		while(!indexQueue.isEmpty()) {
			int index = indexQueue.poll();
			pollCount++;
			currentWord = words.get(index);
			//ui.sendMessage("DEQUEUE: " + currentWord);
			System.out.println("DEQUEUE: " + currentWord);
			for(int i = 0; i < words.size(); i++) {
				if(!(words.get(i).equals(start)) && (parents[i] == -1 || numSteps(parents, index) + 1 < numSteps(parents, i)) && offBy1(words.get(i), currentWord)) {
					
					if(parents[i] != -1) {
						indexQueue.remove(i);
					}
					
					parents[i] = index;
					indexQueue.offer(i);
					
					if(words.get(i).equals(target)) {
						ui.sendMessage("Start Word: " + start + "\n Target: " + target + "\n Poll Count: " + pollCount);
						
						String str = target;
						int wordIndex = find(target);
						int wordSteps = numSteps(parents, wordIndex);
						while(parents[wordIndex] != -1) {
							str = words.get(parents[wordIndex]) + "\n" + str;
							wordIndex = parents[wordIndex];
						}
						
						ui.sendMessage("Path from Start to Target : " + "\n" + str);
						ui.sendMessage("Number of Steps: " + wordSteps);
						return;
					}
				}
				//ui.sendMessage("ENQUEUE: " + s);
				/*if (target.equals(words.get(targetIndex)))
					break;*/
			}
		}
		/*Stack<String> printStack = new Stack<String>();
		printStack.push(target);
		printStack.push(target);
		while(!words.get(parents[targetIndex]).equals(start)) {
			printStack.push(words.get(parents[targetIndex]));
			targetIndex = parents[targetIndex];
		}
		String solution = start + "\n";
		while(!printStack.isEmpty())
			solution = solution + printStack.pop() + "\n";
		ui.sendMessage(solution);
		ui.sendMessage("Poll Count: " + pollCount);
		ui.sendMessage("Target is : " + numSteps(parents, targetIndex) + " steps far");*/
		/*System.out.println(solution);
		System.out.println();
		System.out.println("Poll Count: " + pollCount);*/     
	}
	
	static boolean offBy1(String str1, String str2) {
		if (str1.length() == str2.length()) {
			char[] first = str1.toLowerCase().toCharArray();
			char[] last = str2.toLowerCase().toCharArray();
			
			int count = 0;
			for(int i = 0; i < str1.length(); i++) {
				if (first[i] != last[i])
					count++;
			}
			
			if (count == 1)
				return true;
		}
		return false;	
	}
	
	void loadWords(String fileName) {
		try {
			Scanner in = new Scanner(new File(fileName));
			while(in.hasNextLine()) {
				words.add(in.nextLine());
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	static int find(String word) {
		for(int i=0; i < words.size(); i++) {
			if (words.get(i).equals(word))
				return i;
		}
		return -1;
	}
	
	static int numSteps (int[] parents, int index) {
		
		int steps = 0;
		while(parents[index] != -1) {
			index = parents[index];
			steps++;
		}
		return steps;
	}
	
	static int numDifferent(String str1, String str2) {
		/*int length = (str1.length() < str2.length()) ? str1.length() : str2.length();
		
		char[] first = str1.toLowerCase().toCharArray();
		char[] second = str2.toLowerCase().toCharArray();
		
		int count = 0;
		for(int i = 0; i < length; i++) {
			if (first[i] != second[i])
				count++;
		}
		
		count += (str1.length() > str2.length()) ? (str1.length()-length) : (str2.length()-length);
		
		return count;*/
		int count = 0;
		for (int i=0; i < str1.length(); i++) {
			if(str1.charAt(i) != str2.charAt(i)){
				count++;
			}
		}
		return count;
	}
	
	class IndexComparator implements Comparator<Integer> {
		
		private int[] parents;
		private String target;
		
		public IndexComparator(int[] parents, String target) {
			this.parents = parents;
			this.target = target;
		}
		
		public int sumNums(int index) {
			return numSteps(parents, index) + numDifferent(words.get(index), target);
		}
		
		@Override
		public int compare(Integer index1, Integer index2) {
			return sumNums(index1) - sumNums(index2);
		}
		
	}
}
