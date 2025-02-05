package org.springframework.samples.petclinic.scraper;

import java.util.List;

public class GoogleBooksResponse {
	private Integer totalItems;
	private List<GoogleBook> items;

	public Integer getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}

	public List<GoogleBook> getItems() {
		return items;
	}

	public void setItems(List<GoogleBook> items) {
		this.items = items;
	}
}
