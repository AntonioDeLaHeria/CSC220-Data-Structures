package prog02;

public class SortedPD extends ArrayBasedPD {
	/** Find an entry in the directory.
    @param name The name to be found
    @return the index of the entry with that name or, if it is not
    there, (-insert_index - 1), where insert_index is the index
    where should be added
	 */
	protected int find (String name) {
		/*for (int i = 0; i < size; i++)
			if (theDirectory[i].getName().equals(name))
				return i;

		return -size - 1;*/
		
		int index = 0;
		int first = 0, last = size-1, middle = 0;
		
		while (first <= last) {
			middle = (first + last) / 2;
			index = middle;
			
			if (theDirectory[middle].getName().compareToIgnoreCase(name) < 0) {
				first = middle + 1;
			} else if (theDirectory[middle].getName().compareToIgnoreCase(name) > 0) {
				last = middle - 1;
			} else
				return index;
			
		}
		
		return -index-1;
	}

	/** Add an entry to the directory.
    @param index The index at which to add the entry in theDirectory.
    @param name The name in the new entry.
    @param number The number in the new entry.
    @return The DirectoryEntry that was just added.
	 */
	protected DirectoryEntry add (int index, String name, String number) {
		// index = 0;
		// index = size/2;
		if (size == theDirectory.length)
			reallocate();
		//theDirectory[size] = theDirectory[index];
		//theDirectory[index] = new DirectoryEntry(name, number);
		for (int i = size - 1; i >= index; i--) {
			theDirectory[i+1] = theDirectory[i];
		}
		theDirectory[index] = new DirectoryEntry(name, number);
		size++;
		return theDirectory[index];
	}

	/** Remove an entry from the directory.
    @param index The index in theDirectory of the entry to remove.
    @return The DirectoryEntry that was just removed.
	 */
	protected DirectoryEntry remove (int index) {
		DirectoryEntry entry = theDirectory[index];
		for ( ; index < size -1; index++) { 
			theDirectory[index] = theDirectory [index + 1]; 
		}
		//theDirectory[index] = theDirectory[size-1];
		size--;
		return entry;
	}

	

}
