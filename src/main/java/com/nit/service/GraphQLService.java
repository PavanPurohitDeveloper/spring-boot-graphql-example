package com.nit.service;

import com.nit.model.Book;
import com.nit.repository.BookRepository;
import com.nit.service.datafetcher.AllBooksDataFetcher;
import com.nit.service.datafetcher.BookDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * There is a concept of DataFetcher in the GraphQL.
 * We can create a GraphQL Service.. This service needs to load the data..
 * whatever schema we created, we need to Inject that into Service.
 */
@Service
public class GraphQLService {

    @Autowired
    BookRepository bookRepository; //we need to load the data into bookRepository

    //Inject the GraphQL schema
    @Value("classpath:books.graphql")
    Resource schemaResource;

    //Inject the GraphQL..
    // GraphQL is a @PublicApi provided by GraphQL team for our usage. so we need to execute any query on this particular API.
    private GraphQL graphQL;

    //Inject it so we can create services for it
    @Autowired
    private AllBooksDataFetcher allBooksDataFetcher;

    @Autowired
    private BookDataFetcher bookDataFetcher;

    //PostConstruct which is going to be called when we have the GraphQLService is initialized.
    //load the schema into GraphQL
    @PostConstruct
    private void loadSchema() throws IOException {

        //Before autowiring the GraphQL, we are going to load the data into HSQL database
        loadDataIntoHSQL();

        //first we get the file..get the schema
        File schemaFile = schemaResource.getFile();
        //parse the schema using TypeDefinitionRegistry
        TypeDefinitionRegistry registry = new SchemaParser().parse(schemaFile);
        //we need to do RuntimeWiring..we need to wire the schemas what needs to map to what..
        RuntimeWiring wiring = buildRunTimeWriting();
        //Once the wiring is done, we need to provide wiring and registry to the GraphQLSchema and make it executable.
        //Give this RuntimeWiring to GraphQLSchema..pass the registry and wriring
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
        //pass the schema here.. same schema we need to build it as a GraphQL
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    //load the data into Book repository
    private void loadDataIntoHSQL() {
        // load book information..we need to provide books..from the Books we going to do Stream
        //creating 3 differents  and Injecting that into respository
        Stream.of(
                new Book("123", "Book of clouds", "Kindle Edition",
                        new String[] { "Chloe Aridils"}, "Nov 2017"),
                new Book("124", "Cloud Arch & Engineering", "Orielly",
                        new String[] { "Peter", "Sam"}, "Jan 2015"),
                new Book("125", "Java 9 Programming", "Orielly",
                        new String[] { "Venkat", "Ram"}, "Dec 2016")
        ).forEach(book -> {
            bookRepository.save(book); //Injecting that into respository
        });
    }

    private RuntimeWiring buildRunTimeWriting() {
        //This is going to build RuntimeWiring at runtime.
        return RuntimeWiring.newRuntimeWiring()
                //we need to provide what type of wiring we need to add..
                //we need to add some DataFetchers for allBooks and book methods from GraphQL schema ,
                //how does the API or GraphQL know which table to get what data from ? that is what we need to map in the RuntimeWiring
                //Now we need to add the schema type and we need to add these allBooks and book to that particular schema type.
                //we need to add type = Query and what type of operation or typeWiring to perform..
                //typeWiring we need to provide different data structures
                .type("Query", typeWiring -> typeWiring
                            //we need to provide the mapping for the schema to fetch the data.. If some body calls allBooks in the Query how does it fetch the data
                            .dataFetcher("allBooks", allBooksDataFetcher)
                            .dataFetcher("book", bookDataFetcher))
                .build();
    }

    //Now we have to provide the service for the resouce GraphQL
    //we are exposing the getGraphQL to provide the API back to resource/controller here
    public GraphQL getGraphQL() {
        return graphQL;
    }
}
