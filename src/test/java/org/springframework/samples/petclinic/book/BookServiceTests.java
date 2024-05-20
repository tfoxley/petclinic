package org.springframework.samples.petclinic.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookServiceTests {

	private Owner owner;

	@BeforeEach
	void setup() {
		owner = new Owner();
		owner.setId(1);
		owner.setFirstName("Test");
		owner.setLastName("Tester");
		owner.setCity("Testerville");
		owner.setTelephone("5555555555");

		PetType petType = new PetType();
		petType.setName("Dog");
		Pet pet = new Pet();
		pet.setType(petType);
		pet.setName("Fido");

		owner.addPet(pet);
	}


	@Test
	void testGetRelevantBooks() {
		BookService bookService = new BookService();
		List<Book> books = bookService.getRelevantBooks(owner);
		assertThat(books.size()).isEqualTo(2);
	}
}
