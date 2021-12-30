package com.nit.service.datafetcher;

import com.nit.model.Book;
import com.nit.repository.BookRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

//These data fetchers need to component. they need to implement DataFetcher Interface
//How GraphQL does that is Based on the parameters passed from the schema it automatically filters it out.
@Component
public class AllBooksDataFetcher implements DataFetcher<List<Book>> {

    @Autowired
    BookRepository bookRepository; //This is going to get the data from database

    @Override
    public List<Book> get(DataFetchingEnvironment dataFetchingEnvironment) {
        //This is going to retrieve all the data from the database
        return (List<Book>) bookRepository.findAll();
    }
}
