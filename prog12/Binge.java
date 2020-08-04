package prog12;

import java.util.*;

public class Binge implements SearchEngine {
	HardDisk<PageFile> pageDisk = new HardDisk<PageFile>(); // to store
															// infomation about
															// the web pages
	PageTrie urlToIndex = new PageTrie();

	// store the words as indexes
	// instructions 7
	HardDisk<List<Long>> wordDisk = new HardDisk<List<Long>>();
	// create word to index table
	WordTable wordToIndex = new WordTable();

	public Long indexWord(String word) {

		long index = wordDisk.newFile();
		List<Long> wordPages = new ArrayList<Long>();

		System.out.println(" indexing word " + index + " (" + word + ") " + wordPages);
		wordDisk.put(index, wordPages);
		wordToIndex.put(word, index);

		return index;
	}

	public Long indexPage(String URL) {

		long index = pageDisk.newFile();
		PageFile newFile = new PageFile(index, URL);

		System.out.println(" indexing page " + newFile);
		pageDisk.put(index, newFile);
		urlToIndex.put(URL, index);

		return index;
	}

	public void gather(Browser browser, List<String> startingURLs) {
		System.out.println(" gather " + startingURLs);
		Queue<Long> pgQue = new ArrayDeque<Long>();

		for (String URL : startingURLs) {
			if (!urlToIndex.containsKey(URL)) {
				long temIndex = indexPage(URL);
				pgQue.offer(temIndex);
			}
		}

		while (!pgQue.isEmpty()) {
			System.out.println(" queue " + pgQue);

			long indexPage = pgQue.poll();
			PageFile filePage = pageDisk.get(indexPage);

			System.out.println(" dequeued " + filePage);
			// I get the page
			if (browser.loadPage(filePage.url)) {

				// A B bunch of URLS stored in list
				List<String> urlList = browser.getURLs();

				Set<Long> pageIndexes = new HashSet<Long>(); // to avoid
																// duplicates

				Long urlIndex;

				System.out.println(" urls: " + urlList);
				for (String url : urlList) {
					if (!urlToIndex.containsKey(url)) {
						urlIndex = indexPage(url);
						pgQue.offer(urlIndex);
					} else {
						urlIndex = urlToIndex.get(url);
					}
					pageIndexes.add(urlIndex);
				}

				for (Long index : pageIndexes) {
					pageDisk.get(index).incRefCount();
					System.out.println(" inc ref: " + pageDisk.get(index));
				}

				List<String> wordList = browser.getWords();

				Set<Long> wordIndexes = new HashSet<Long>();

				Long wordIndex;

				System.out.println(" words: " + wordList);
				for (String word : wordList) {
					if (!wordToIndex.containsKey(word)) {
						wordIndex = indexWord(word);
					} else {
						wordIndex = wordToIndex.get(word);
					}

					// check if the word is not in the wordIndexes set to avoid
					// duplicates
					if (!wordIndexes.contains(wordIndex)) {
						wordIndexes.add(wordIndex);
						wordDisk.get(wordIndex).add(indexPage);
						System.out.println(" add page: " + wordIndex + " (" + word + ") " + wordDisk.get(wordIndex));

					}
				}
			}
		}
		System.out.println(" pageDisk " + "\n" + pageDisk);
		System.out.println(" urlToIndex " + "\n" + urlToIndex);
		System.out.println(" wordDisk " + "\n" + wordDisk);
		System.out.println(" wordToIndex " + "\n" + wordToIndex);

		urlToIndex.write(pageDisk);
		wordToIndex.write(wordDisk);
	}

	@Override
	public String[] search(List<String> keyWords, int numResults) {
		// TODO Auto-generated method stub
		return new String[0];
	}
}
