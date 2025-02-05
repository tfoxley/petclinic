package org.springframework.samples.petclinic.book;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {

	public static final int RESULT_LIMIT = 5;

	private final BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	/**
	 * Returns a list of the top 5 books relevant to the owner's pets (based on pet type).
	 * @param owner A pet owner object for which to find relevant books.
	 * @return A list of books relevant to the owner's pets.
	 */
	public List<Book> getRelevantBooks(Owner owner) {
		List<Book> relatedBooks = new ArrayList<>();
		for (Pet pet : owner.getPets()) {
			List<Book> allBooks = bookRepository.findByPetType(pet.getType().getName());
			if (!allBooks.isEmpty()) {
				for (int i = 0; i < Math.min(RESULT_LIMIT, allBooks.size()); i++) {
					relatedBooks.add(allBooks.get(i));
				}
			}
		}
		return relatedBooks;
	}
}
