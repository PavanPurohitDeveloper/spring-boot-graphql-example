package com.nit.service.datafetcher;

import com.nit.model.Book;
import com.nit.repository.BookRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

//These data fetchers need to component. they need to implement DataFetcher Interface
@Component
public class BookDataFetcher implements DataFetcher<Book> {

    @Autowired
    BookRepository bookRepository; //This is going to get the data from database

    @Override
    public Book get(DataFetchingEnvironment dataFetchingEnvironment) {
        String isn = dataFetchingEnvironment.getArgument("id");
        Optional<Book> bookOptional = bookRepository.findById(isn);
        //Optional<String> isnD = bookOptional.of("id");
        //System.out.println(bookOptional.get());
        return bookOptional.get();
    }
}
