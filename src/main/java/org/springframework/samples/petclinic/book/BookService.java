package org.springframework.samples.petclinic.book;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class BookService {

	public static final int RESULT_LIMIT = 5;

	public List<Book> getRelevantBooks(Owner owner) {
		List<String> keywords = new ArrayList<>();
		for (Pet pet : owner.getPets()) {
			keywords.add(pet.getType().getName());
		}
		return fetchBooksFromAPI(keywords);
	}

	/**
	 * Uses the open library API to get book results.
	 *
	 * Full API Docs found at https://openlibrary.org/dev/docs/api/search
	 *
	 * Abbreviated Docs:
	 *
	 * URL PARAMETERS
	 * Parameter		Description
	 * q				The solr query. See Search HowTo for sample queries
	 * fields			The fields to get back from solr. Use the special value * to get all fields (although be prepared for a very large response!).
 	 * 					To fetch availability data from archive.org, add the special value, availability. Example: /search.json?q=harry%20potter&fields=*,availability&limit=1. This will fetch the availability data of the first item in the `ia` field.
	 * sort				You can sort the results by various facets such as new, old, random, or key (which sorts as a string, not as the number stored in the string). For a complete list of sorts facets look here (this link goes to a specific commit, be sure to look at the latest one for changes). The default is to sort by relevance.
	 * lang				The users language as a two letter (ISO 639-1) language code. This influences but doesn't exclude search results. For example setting this to fr will prefer/display the French edition of a given work, but will still match works that don't have French editions. Adding language:fre on the other hand to the search query will exclude results that don't have a French edition.
	 * offset / limit	Use for pagination.
	 * page / limit		Use for pagination, with limit corresponding to the page size. Note page starts at 1.
	 *
	 *
	 * RESPONSE FORMAT
	 * {
	 *     "start": 0,
	 *     "num_found": 629,
	 *     "docs": [
	 *         {...},
	 *         {...},
	 *         ...
	 *         {...}
	 *     ]
	 * }
	 *
	 * Each document specified listed in "docs" will be of the following format:
	 * {
	 *     "cover_i": 258027,
	 *     "has_fulltext": true,
	 *     "edition_count": 120,
	 *     "title": "The Lord of the Rings",
	 *     "author_name": [
	 *         "J. R. R. Tolkien"
	 *     ],
	 *     "first_publish_year": 1954,
	 *     "key": "OL27448W",
	 *     "ia": [
	 *         "returnofking00tolk_1",
	 *         "lordofrings00tolk_1",
	 *         "lordofrings00tolk_0",
	 *     ],
	 *     "author_key": [
	 *         "OL26320A"
	 *     ],
	 *     "public_scan_b": true
	 * }
	 */
	private List<Book> fetchBooksFromAPI(List<String> keywords) {
		List<Book> allBooks = new ArrayList<>();
		for (String keyword : keywords) {
			RestTemplate restTemplate = new RestTemplate();
			Map<String, Object> results = restTemplate.getForObject("https://openlibrary.org/search.json?q=" + keyword,
					HashMap.class);
			if (results != null) {
				List<Map<String, Object>> resultDocs = (List<Map<String, Object>>) results.get("docs");
				if (resultDocs != null && resultDocs.size() > 0) {
					Iterator<Map<String, Object>> resultsIter = resultDocs.iterator();
					int counter = 0;
					while (resultsIter.hasNext() && counter < RESULT_LIMIT) {
						Map<String, Object> result = resultsIter.next();
						Book book = new Book();
						book.setTitle((String) result.get("title"));
						List<String> authorNames = (List<String>) result.get("author_name");
						book.setAuthor(authorNames.get(0));
						allBooks.add(book);
						counter++;
					}
				}
			}
		}
		return allBooks;
	}

}
