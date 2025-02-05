package org.springframework.samples.petclinic.scraper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

@RestController
public class ScraperController {
	private static final String API_KEY = "";
	private static final int MAX_RESULTS = 40;

	private final RestTemplate restTemplate;

	public ScraperController() {
		this.restTemplate = new RestTemplate();
	}

	@GetMapping("/scrape/{petType}")
	public void scrape(@PathVariable String petType) {
		GoogleBooksResponse response = fetchBooks(petType, 0);
		if (response != null && response.getTotalItems() > 0) {
			try (PrintWriter writer = new PrintWriter(petType + ".sql")) {
				processResults(response, writer, petType);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private GoogleBooksResponse fetchBooks(String keyword, int startIndex) {
		return restTemplate.getForObject(
			"https://www.googleapis.com/books/v1/volumes?q=" + keyword +
			"&startIndex=" + startIndex +
			"&maxResults=" + MAX_RESULTS +
			"&key=" + API_KEY, GoogleBooksResponse.class
		);
	}

	private void processResults(GoogleBooksResponse response, PrintWriter writer, String petType) {
		for (GoogleBook googleBook : response.getItems()) {
			VolumeInfo volumeInfo = googleBook.getVolumeInfo();

			StringBuffer buffer = new StringBuffer("INSERT INTO books VALUES (default, ");
			buffer.append("'");
			if (volumeInfo.getAuthors() != null && !volumeInfo.getAuthors().isEmpty()) {
				buffer.append(volumeInfo.getAuthors().get(0).replace("'", "''"));
			} else {
				buffer.append("Unknown");
			}
			buffer.append("', ");

			buffer.append("'");
			buffer.append(volumeInfo.getTitle().replace("'", "''"));
			buffer.append("', ");

			buffer.append("'");
			buffer.append(petType);
			buffer.append("', ");

			buffer.append("'");
			if (volumeInfo.getPublishedDate() != null) {
				buffer.append(volumeInfo.getPublishedDate());
			} else {
				buffer.append("Unknown");
			}
			buffer.append("', ");

			buffer.append("'");
			if (volumeInfo.getPublisher() != null) {
				buffer.append(volumeInfo.getPublisher().replace("'", "''"));
			} else {
				buffer.append("Unknown");
			}
			buffer.append("', ");

			buffer.append("'");
			if (volumeInfo.getPageCount() != null) {
				buffer.append(volumeInfo.getPageCount());
			} else {
				buffer.append(0);
			}
			buffer.append("'");

			buffer.append(");");
			writer.println(buffer);
		}
	}
}
