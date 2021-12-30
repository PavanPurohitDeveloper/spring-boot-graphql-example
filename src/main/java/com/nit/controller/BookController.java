package com.nit.controller; //we can create either package name as controller or resource

import com.nit.service.GraphQLService;
import graphql.ExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//BookController or BookResource..exposing the /rest/books
@RequestMapping("/rest/books")
@RestController
public class BookController {

    @Autowired
    private GraphQLService graphQLService;

    //RequestBody we are getting as a Query
    @PostMapping
    public ResponseEntity<Object> getAllBooks(@RequestBody String query) {
        //from the graphQLService to get the GrapQL publicAPI  to execute the query, using execute method we provide query to execute it
        //query which will be providing from the postman..to push the response back we use ExecutionResult.
        //query needs to be provided to GrapQL API.. In order to create a GrapQL API we using GraphQLService
        ExecutionResult executionResult = graphQLService.getGraphQL().execute(query);
        //Wrap the whole execution result into the ResponseEntity object
        return new ResponseEntity<>(executionResult, HttpStatus.OK);
    }


}
