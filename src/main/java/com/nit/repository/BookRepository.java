package com.nit.repository;

import com.nit.model.Book;
import org.springframework.data.repository.CrudRepository;

//BookRepository is to query the book
public interface BookRepository extends CrudRepository<Book, String > {

}
