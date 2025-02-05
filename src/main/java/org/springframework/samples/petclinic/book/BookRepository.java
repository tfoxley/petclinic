package org.springframework.samples.petclinic.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

	@Transactional(readOnly = true)
	List<Book> findByPetType(String petType);
}
