package prog02;

/**
 * A program to query and modify the phone directory stored in csc220.txt.
 * @author vjm
 */

public class Main {
	/** Processes user's commands on a phone directory.
    @param fn The file containing the phone directory.
    @param ui The UserInterface object to use
    to talk to the user.
    @param pd The PhoneDirectory object to use
    to process the phone directory.
	 */
	public static void processCommands(String fn, UserInterface ui, PhoneDirectory pd) {
		pd.loadData(fn);

		String[] commands = {
				"Add/Change Entry",
				"Look Up Entry",
				"Remove Entry",
				"Save Directory",
		"Exit"};

		String name, number, oldNumber;

		while (true) {
			int c = ui.getCommand(commands);
			switch (c) {
			case -1:
				ui.sendMessage("You shut down the program, restarting.  Use Exit to exit.");
				break;
			case 0:
				name = ui.getInfo(" Enter the name"); 
				
				if (name != null) {
					if (!name.isEmpty()) {
						number = ui.getInfo(" Enter the number");
						oldNumber = pd.lookupEntry(name);
						if (number != null){
							pd.addOrChangeEntry(name, number); 
							pd.save();
							if (oldNumber == null)
								ui.sendMessage(name +" has been added with number: " + number);
							else
								ui.sendMessage("The number for " + name + " has been changed from " + oldNumber + " to " + number);
						}
					} else {
						ui.sendMessage("No blank names please!");
					}
				}

				/*String[] options = { "add", "change"};
				int choice = ui.getCommand(options); 
				switch(choice) { 
				case 0: 
					// ui.sendMessage(" Add new contact");
					 
					String[] yourChoice = {"save" ,"cancel"}; 
					int know = ui.getCommand(yourChoice); 
					switch (know) { 
					case 0: 
						
						ui.sendMessage(" new contact has been saved");
						
						break; 
					case 1: 
						ui.sendMessage(" your contact has been cancled");
						break; 
					}
					
					
					
					break; 
				case 1:
					// ui.sendMessage(" change existing contact");
					String searchName = ui.getInfo(" seach for name "); 
					if ( searchName == null || searchName.isEmpty() ) { 
						ui.sendMessage(" contact does not exist ");
						
					} else { 
						String changedNumber = ui.getInfo(" Enter new number "); 
						pd.addOrChangeEntry(searchName, changedNumber); 
						pd.save();
						ui.sendMessage(searchName + " contact has been changed" );
					}
					break; 
				}*/
				
				break;
			case 1:
				name = ui.getInfo(" Enter the name "); 
				if (name != null) {
					if (name.isEmpty()) {
						ui.sendMessage("No blank names please!");
					} else {
						number = pd.lookupEntry(name); 
						if (number == null) { 
							ui.sendMessage( name + " is not listed ");
						} else { 
							ui.sendMessage(name + " has number" + number);
						}
					}
				}
				/*if (name == null || name.isEmpty()) { 
					ui.sendMessage(" please enter a valid name");
				} else { 
					number = pd.lookupEntry(name); 
					if (number == null) { 
						ui.sendMessage( name + " is not listed ");
					} else { 
						ui.sendMessage(name + " has number" + number);
					}
				}*/
				break;
			case 2:
				name = ui.getInfo(" Enter a name "); 
				if (name != null) {
					if (name.isEmpty()) {
						ui.sendMessage("No blank names please!");
					} else {
						number = pd.lookupEntry(name);
						if (number != null) {
							pd.removeEntry(name); 
							pd.save();
							ui.sendMessage("Removed entry with name " + name + " and number " + number);
						} else {
							ui.sendMessage(name + " is not listed");
						}
					}
				}
				/*if (name == null || name.isEmpty()) { 
					ui.sendMessage(" Contact does not exist ");
				} else { 
					number = pd.lookupEntry(name);
					if (number != null) {
						pd.removeEntry(name); 
						pd.save();
						ui.sendMessage("Removed entry with name " + name + " and number " + number);
					} else {
						ui.sendMessage(name + " is not listed");
					}
				}*/
				
				break;
			case 3:
				pd.save();
				ui.sendMessage("file has been saved");
				break;
			case 4:
				ui.sendMessage("Goodbye");
				return;
			}
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		String fn = "csc220.txt";
		PhoneDirectory pd = new ArrayBasedPD();
		//UserInterface ui = new GUI("Phone Directory");
		UserInterface ui = new TestUI();
		processCommands(fn, ui, pd);
	}

}
