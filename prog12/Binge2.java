package prog12;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class Binge2 implements SearchEngine {

	HardDisk<PageFile> pageDisk = new HardDisk<PageFile>();
	PageTrie pageToIndex = new PageTrie();

	HardDisk<List<Long>> wordDisk = new HardDisk<List<Long>>();
	WordTable wordToIndex = new WordTable();

	@Override
	public void gather(Browser browser, List<String> startingURLs) {

		pageToIndex.read(pageDisk);
		wordToIndex.read(wordDisk);
	}

	@Override
	public String[] search(List<String> keyWords, int numResults) {

		Iterator<Long>[] wordFileIterators = (Iterator<Long>[]) new Iterator[keyWords.size()];

		long[] currentPageIndices = new long[keyWords.size()];

		PriorityQueue<Long> bestPageIndices = new PriorityQueue<Long>(numResults, new PageComparator());

		int itrIndex = 0;
		for (String keyword : keyWords) {
			if (!wordToIndex.containsKey(keyword)) {
				return new String[0];
			} else {
				long wordIndex = wordToIndex.get(keyword);
				wordFileIterators[itrIndex] = wordDisk.get(wordIndex).iterator();
				itrIndex++;
			}
		}

		while (getNextPageIndices(currentPageIndices, wordFileIterators)) {
			if (allEqual(currentPageIndices)) {
				bestPageIndices.offer(currentPageIndices[0]);
			}
		}

		String[] finalResult = new String[bestPageIndices.size()];
		for (int i = finalResult.length - 1; i >= 0; i--) {
			finalResult[i] = pageDisk.get(bestPageIndices.poll()).url;
		}

		return finalResult;
	}

	public class PageComparator implements Comparator<Long> {
		@Override
		public int compare(Long o1, Long o2) {
			return pageDisk.get(o1).getRefCount() - pageDisk.get(o2).getRefCount();
		}
	}

	private boolean getNextPageIndices(long[] currentPageIndices, Iterator<Long>[] wordFileIterators) {
		long largetPage = currentPageIndices[0];

		if (allEqual(currentPageIndices)) {
			for (int i = 0; i < wordFileIterators.length; i++) {
				if (wordFileIterators[i].hasNext()) {
					currentPageIndices[i] = wordFileIterators[i].next();
				} else
					return false;
			}
		} else {
			for (Long ind : currentPageIndices) {
				if (ind > largetPage)
					largetPage = ind;
			}

			for (int i = 0; i < currentPageIndices.length; i++) {
				if (currentPageIndices[i] < largetPage) {

					if (wordFileIterators[i].hasNext()) {
						currentPageIndices[i] = wordFileIterators[i].next();
					} else
						return false;
				}
			}
			return true;
		}

		return true;
	}

	private boolean allEqual(long[] array) {
		long first = array[0];
		for (int i = 1; i < array.length; i++) {
			if (first != array[i])
				return false;
		}
		return true;
	}

}
