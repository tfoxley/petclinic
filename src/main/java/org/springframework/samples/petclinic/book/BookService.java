package org.springframework.samples.petclinic.book;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class BookService {

	public List<Book> getRelevantBooks(Owner owner) {
		List<String> keywords = new ArrayList<>();
		for (Pet pet : owner.getPets()) {
			keywords.add(pet.getType().getName());
		}
		return fetchBooksFromAPI(keywords);
	}

	// API docs: https://openlibrary.org/dev/docs/api/search
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
					while (resultsIter.hasNext() && counter < 5) {
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
