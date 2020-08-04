package prog11;

import java.util.*;
import prog02.*;

public class Trie<V> extends AbstractMap<String, V> {

	private class Entry<V> implements Map.Entry<String, V> {
		String key;
		V value;

		Entry(String key, V value) {
			this.key = key;
			this.value = value;
			this.sub = key;
		}

		Entry(String sub, String key, V value) {
			this.key = key;
			this.value = value;
			this.sub = sub;
		}

		Entry(String sub, String key, V value, List<Entry<V>> list) {
			this.key = key;
			this.value = value;
			this.sub = sub;
			this.list = list;
		}

		public String getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		public V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		String sub;
		List<Entry<V>> list = new ArrayList<Entry<V>>();

		@Override
		public String toString() {
			return "Entry [key=" + key + ", value=" + value + ", sub=" + sub + ", list=" + list + "]";
		}
	}

	private List<Entry<V>> list = new ArrayList<Entry<V>>();
	private int size;

	public int size() {
		return size;
	}

	/**
	 * Find the entry with the given key.
	 * 
	 * @param key  The key to be found.
	 * @param iKey The current starting character index in the key.
	 * @param list The list of entrys to search for the key.
	 * @return The entry with that key or null if not there.
	 */
	private Entry<V> find(String key, int iKey, List<Entry<V>> list) {
		// EXERCISE:
		int iEntry = -1;
		// Set iEntry to the index of the entry in list such that the first
		// character (charAt(0)) of its sub (NOT key) is the same as the
		// character at index iKey in key.
		// If no such entry, return null.
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).sub.charAt(0) == key.charAt(iKey)) {
				iEntry = i;
				break;
			}
		}

		if (iEntry == -1)
			return null;

		Entry<V> entry = list.get(iEntry);
		int iSub = 0;

		// If we are here, then character iKey of key and iSub of entry.sub
		// must match. Increment both iKey and iSum until that is not true.
		while (iSub < entry.sub.length() && iKey < key.length() && entry.sub.charAt(iSub) == key.charAt(iKey)) {
			iSub++;
			iKey++;
		}

		// If we have not matched all the characters of entry.sub, the key
		// is not in the Trie.
		if (iKey < key.length()) {
			return find(key.substring(iKey, key.length()), 0, entry.list);
		} else if (entry.key != null && iKey == key.length() && iSub == entry.sub.length()) {
			return entry;
		}

		// If we have matched all the characters of key, then entry might
		// be the one we want (to return). But only if its key is not
		// null. If it is null, the key is not in the Trie.
		/*
		 * if (entry.key != null && entry.key == key) return entry;
		 */

		// If we have not returned yet, we need to recurse, but if
		// entry.list is null, we cannot so the key is not in the Trie.

		return null;
	}

	public boolean containsKey(Object key) {
		Entry<V> entry = find((String) key, 0, list);
		return entry != null && entry.key != null;
	}

	public V get(Object key) {
		Entry<V> entry = find((String) key, 0, list);
		if (entry != null && entry.key != null)
			return entry.value;
		return null;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	private V put(String key, int iKey, V value, List<Entry<V>> list) {
		int iNode = 0;
		char keyChar = key.charAt(iKey);

		while (iNode < list.size() && keyChar > list.get(iNode).sub.charAt(0)) {
			iNode++;
		}
		if (iNode == list.size() || keyChar < list.get(iNode).sub.charAt(0)) {
			Entry<V> node = new Entry(key.substring(iKey), key, value);
			list.add(iNode, node);
			size++;
			return null;
		}

		// Again, match as much as possible.
		Entry<V> node = list.get(iNode);
		iKey++;
		int iSub = 1;
		while (iKey < key.length() && iSub < node.sub.length() && key.charAt(iKey) == node.sub.charAt(iSub)) {
			iKey++;
			iSub++;
		}

		// If iKey and iSub are both out of range...
		// (Don't forget to set node.key if it was just a placeholder.)
		if (iKey == key.length() && iSub == node.sub.length()) {
			if (node.key == null) {
				node.key = key;
				size++;
			}
			return node.setValue(value);
		}

		// If just iSub is out of range, easy recursion.
		if (iSub == node.sub.length()) {
			return put(key, iKey, value, node.list);
		}

		// Create the new node, insert it into the current node's list,
		// and adjust the current node's sub, key, and value.
		Entry<V> nNode = new Entry(node.sub.substring(iSub), node.key, node.value);
		nNode.list = node.list;
		node.list = new ArrayList<Entry<V>>();
		node.sub = node.sub.substring(0, iSub);
		node.key = null;
		node.value = null;
		node.list.add(nNode);

		// Easy case if iKey is out of range.
		if (iKey == key.length()) {
			node.key = key;
			node.value = value;
			size++;
		} else {
			return put(key, iKey, value, node.list);
		}

		// Otherwise recurse.

		return null;
	}

	public V put(String key, V value) {
		return put(key, 0, value, list);
	}

	public V remove(Object keyAsObject) {
		if (keyAsObject != null || keyAsObject.toString().length() > 0 || containsKey(keyAsObject.toString()) == false)
			return remove(keyAsObject.toString(), 0, list);
		return null;
	}

	private V remove(String key, int iKey, List<Entry<V>> list) {
		int iEntry = -1;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).sub.charAt(0) == key.charAt(iKey)) {
				iEntry = i;
				break;
			}
		}

		if (iEntry == -1)
			return null;

		Entry<V> entry = list.get(iEntry);
		int iSub = 0;

		while (iSub < entry.sub.length() && iKey < key.length() && entry.sub.charAt(iSub) == key.charAt(iKey)) {
			iSub++;
			iKey++;
		}

		if (iKey == key.length() && iSub == entry.sub.length()) {
			list.remove(iEntry);
			return entry.value;
		} else if (iKey < key.length()) {
			V value = remove(key.substring(iKey, key.length()), 0, entry.list);
			if (entry.list.size() == 1 && entry.key == null) {
				entry.sub = entry.sub + entry.list.get(0).sub;
				entry.key = entry.list.get(0).key;
				entry.value = entry.list.get(0).value;
				entry.list = new ArrayList<Trie<V>.Entry<V>>();
			}
			return value;
		}

		return null;
	}

	private Iterator<Map.Entry<String, V>> entryIterator() {
		return new EntryIterator();
	}

	private class EntryIterator implements Iterator<Map.Entry<String, V>> {
		// EXERCISE
		Stack<Iterator<Entry<V>>> stack = new Stack<Iterator<Entry<V>>>();

		EntryIterator() {
			stack.push(list.iterator());
		}

		public boolean hasNext() {
			// EXERCISE
			// While the iterator at the top of the stack has not next, pop it.
			// Return true if you have not popped all the iterators.
			while (stack.isEmpty() == false && !stack.peek().hasNext())
				stack.pop();

			return !stack.empty();
		}

		public Map.Entry<String, V> next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Entry<V> entry = null;
			// EXERCISE
			// Set entry to the next of the top of the stack.
			// If its list is not null, push its iterator.
			// Repeat those two steps if entry.key is null.
			entry = stack.peek().next();
			if (entry.list != null)
				stack.push(entry.list.iterator());

			return entry;
		}

		public void remove() {
		}
	}

	public Set<Map.Entry<String, V>> entrySet() {
		return new EntrySet();
	}

	private class EntrySet extends AbstractSet<Map.Entry<String, V>> {
		public int size() {
			return size;
		}

		public Iterator<Map.Entry<String, V>> iterator() {
			return entryIterator();
		}

		public void remove() {
		}
	}

	public String toString() {
		return toString(list, 0);
	}

	private String toString(List<Entry<V>> list, int indent) {
		String ind = "";
		// Add indent number of spaces to ind:
		for (int i = 0; i < indent; i++)
			ind = ind + " ";

		String s = "";
		for (Entry<V> entry : list) {
			// Append indented entry (sub, key, and value) and newline ("\n") to s.

			s = s + ind + entry.sub + " " + entry.key + " " + entry.value + "\n";

			// If there is a sublist, append its toString to s.
			// What value of indent should you used?
			// bob null null
			// by bobby 7
			// cat bobcat 8
			// What additional indent would make by and cat line up just past bob?
			if (entry.list.size() > 0) {
				indent = indent + 4;
				s = s + toString(entry.list, indent);
			}
		}
		return s;
	}

	void test() {
		Entry<Integer> bob = new Entry<Integer>("bob", null, null);
		list.add((Entry<V>) bob);
		Entry<Integer> by = new Entry<Integer>("by", "bobby", 0);
		bob.list.add(by);
		Entry<Integer> ca = new Entry<Integer>("ca", null, null);
		bob.list.add(ca);
		Entry<Integer> lf = new Entry<Integer>("lf", "bobcalf", 1);
		ca.list.add(lf);
		Entry<Integer> t = new Entry<Integer>("t", "bobcat", 2);
		ca.list.add(t);
		Entry<Integer> catdog = new Entry<Integer>("catdog", "catdog", 3);
		list.add((Entry<V>) catdog);
		size = 4;
	}

	public static void main(String[] args) {
		Trie<Integer> trie = new Trie<Integer>();
		trie.test();
		System.out.println(trie);

		UserInterface ui = new ConsoleUI();

		String[] commands = { "toString", "containsKey", "get", "put", "size", "entrySet", "remove", "quit" };
		String key = null;
		int value = -1;

		while (true) {
			int command = ui.getCommand(commands);
			switch (command) {
			case 0:
				ui.sendMessage(trie.toString());
				break;
			case 1:
				key = ui.getInfo("key: ");
				if (key == null) {
					ui.sendMessage("null key");
					break;
				}
				ui.sendMessage("containsKey(" + key + ") = " + trie.containsKey(key));
				break;
			case 2:
				key = ui.getInfo("key: ");
				if (key == null) {
					ui.sendMessage("null key");
					break;
				}
				ui.sendMessage("get(" + key + ") = " + trie.get(key));
				break;
			case 3:
				key = ui.getInfo("key: ");
				if (key == null) {
					ui.sendMessage("null key");
					break;
				}
				try {
					value = Integer.parseInt(ui.getInfo("value: "));
				} catch (Exception e) {
					ui.sendMessage(value + "not an integer");
					break;
				}
				ui.sendMessage("put(" + key + "," + value + ") = " + trie.put(key, value));
				break;
			case 4:
				ui.sendMessage("size() = " + trie.size());
				break;
			case 5: {
				String s = "";
				for (Map.Entry<String, Integer> entry : trie.entrySet())
					s += entry.getKey() + " " + entry.getValue() + "\n";
				ui.sendMessage(s);
				break;
			}
			case 6:
				key = ui.getInfo("Key: ");
				ui.sendMessage("remove(" + key + ") = " + trie.remove(key));
				break;
			case 7:
				return;
			}
		}
	}
}
