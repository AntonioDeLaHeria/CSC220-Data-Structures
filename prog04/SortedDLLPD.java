package prog04;

public class SortedDLLPD extends DLLBasedPD {

	/**
	 * Add a new entry at a location.
	 * 
	 * @param location
	 *            The location to add the new entry, null to add it at the end
	 *            of the list
	 * @param name
	 *            The name in the new entry
	 * @param number
	 *            The number in the new entry
	 * @return the new entry
	 */
	protected DLLEntry add(DLLEntry location, String name, String number) {
		DLLEntry entry = new DLLEntry(name, number);

		// EXERCISE
		// Add entry to the end of the list.
		if (head == null) {
			head = entry;
			tail = entry;
			return entry;
		} else if (head != null && location == null) {
			location = head;
			while (location != null && location.getNext() != null) {
				location = location.getNext();
			}
			location.setNext(entry);
			entry.setPrevious(location);
			tail = entry;
		} else if (location != null) {
			DLLEntry next = location; 
			DLLEntry previous = location.getPrevious(); 
			if (previous != null) {
				previous.setNext(entry);
			} else {
				head = entry;
			}
			entry.setPrevious(previous);
		    next.setPrevious(entry);
		    entry.setNext(next);
		}

		return entry;
	}

	/**
	 * Find an entry in the directory. If not found, compare it with existing entries, return entry with name greater 'name' 
	 * 
	 * @param name
	 *            The name to be found
	 * @return The entry with the same name or null if it is not there.
	 */
	protected DLLEntry find(String name) {
		// EXERCISE
		// For each entry in the directory.
		// What is the first? What is the next? How do you know you got them
		// all?

		// If this is the entry you want

		// return it.
		if (head == null) {
			return null;
		}
		
		/*if (head != null && head.getName().equals(name)) 
			return head;*/

		if (head != null && (head.getName().compareToIgnoreCase(name) > 0 || head.getName().equals(name))) {
			return head;
		}

		DLLEntry entry = head.getNext();
		while (entry != null) {
			if (entry.getName().compareToIgnoreCase(name) > 0 || entry.getName().equals(name)) 
				return entry;
			else
				entry = entry.getNext();
		}
		// Name not found.
		return entry;
	}
	
	/**
	 * Check if a name is found at a location.
	 * 
	 * @param location
	 *            The location to check
	 * @param name
	 *            The name to look for at that location
	 * @return false, if location is null or it does not have that name; true,
	 *         otherwise.
	 */
	protected boolean found(DLLEntry location, String name) {
		if (location == null || !location.getName().equals(name) ) {
			return false;
		}
		return true;
	}

}
