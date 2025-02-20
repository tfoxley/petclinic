package org.springframework.samples.petclinic.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

	// Using option 1 or 3 at https://springjava.com/spring-data-jpa/how-to-use-limit-query-results/
	// is recommended to avoid performance issues with large datasets.
	@Transactional(readOnly = true)
	List<Book> findByPetType(String petType);
}
