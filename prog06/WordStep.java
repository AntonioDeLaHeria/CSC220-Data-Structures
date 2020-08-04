package prog06;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import prog02.GUI;
import prog02.UserInterface;

public class WordStep {
	static UserInterface ui = new GUI("wordStep");
	static List<String> words = new ArrayList<String>();
	
	public static void main(String[] args) {
		WordStep game = new WordStep(); 
		String s = ui.getInfo("Please enter the File Name:");
		game.loadDictionary(s);
		String start, target;
		while(true) {
			start = ui.getInfo(" Enter starting word: ");
			target = ui.getInfo(" Enter the target word you wish for: ");
			if (find(start) != -1 && find(target) != -1)
				break;
			else {
				ui.sendMessage("The start and the target words are not in the list");
			}
		}
		
		String[] commands = { "Human plays.", "Computer plays." };
		int c = ui.getCommand(commands);
		if(c == 0) { 
			game.play(start, target);
		}
		
	}
	void play (String current, String target) { 
		while (true) { 
			ui.sendMessage("Current postition: " +  current);
			ui.sendMessage("Target is: " + target);
			String nextWord = ui.getInfo(" What is the next word: "); 
			if (offBy1(nextWord, current)) {
				current = nextWord;
			} else {
				ui.sendMessage("Invalid word");
			}
			if (current.equals(target)) {
				ui.sendMessage("You Win!!!");
				return;
			}
		}
		
	}
	void solve (String start, String target) { 
		int[] parents = new int[words.size()];
		for (int i=0; i<parents.length; i++)
			parents[i] = -1;
		Queue<Integer> indexQueue = new ArrayQueue<Integer>();
		indexQueue.add(find(start));
		while(!indexQueue.isEmpty()) {
			int index = indexQueue.poll();
			String currentWord = words.get(index);
			for (int i = 0; i < words.size(); i++) {
				if (!words.get(i).equals(currentWord) && offBy1(words.get(i), currentWord)) {
					indexQueue.add(i);
					parents[i] = find(currentWord);
				}
				if (currentWord.equals(target)) {
					ui.sendMessage("You Win!!!");
					String solution = "";
					Queue<Integer> printQueue = new ArrayQueue<Integer>();
					printQueue.add(find(currentWord));
					while(!printQueue.isEmpty()) {
						int printindex = printQueue.poll();
						solution += words.get(printindex) + " ";
						for (int j=0; j<parents.length; j++) {
							if (parents[j] == printindex)
								printQueue.add(j);
						}
					}
					ui.sendMessage(solution);
					return;
				}
			}
		}
	}
	
	void loadDictionary(String fileName) {
		try {
			Scanner in = new Scanner(new File(fileName));
			while (in.hasNextLine()) {
				words.add(in.nextLine());				
			}
			in.close();
		} catch (FileNotFoundException ex) {
			return;
		} catch (Exception ex) {
			System.err.println("Load of directory failed.");
			ex.printStackTrace();
			System.exit(1);
		}
	}

	
	static boolean offBy1(String str1, String str2) {
		if (str1.length() == str2.length()) {
			char[] first  = str1.toLowerCase().toCharArray();
			char[] second = str2.toLowerCase().toCharArray();
			
			int count = 0;
			for(int i = 0; i < str1.length(); i++)
			{
		        if (first[i] != second[i])
		        {
		            count++;
		        }
			}
			
			if (count == 1) 
				return true;
		}
		
		return false;
	}
	
	static int find(String word) {
		for (int i=0; i < words.size(); i++) {
			if (words.get(i).equals(word))
				return i;
		}
		return -1;
	}
}
