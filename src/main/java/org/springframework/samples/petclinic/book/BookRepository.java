package org.springframework.samples.petclinic.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

	// Spring Docs found at: https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods-details.html#repositories.special-parameters
	@Transactional(readOnly = true)
	List<Book> findByPetType(String petType);
}
