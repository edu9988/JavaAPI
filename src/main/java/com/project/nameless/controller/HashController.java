package com.project.nameless.controller;

import com.project.nameless.model.User;
import com.project.nameless.model.UserService;
import com.project.nameless.exception.ResourceConflictException;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/hash")
public class HashController {

    @Autowired private ApplicationContext context;

    public Map<String, String> show( int uid ) {
	UserService us = context.getBean( UserService.class );
	List <Map<String,Object>> ls = us.getUser(uid);
	System.out.println( "SELECT query returned List with "
	    + ls.size() + " rows" );
	if( ls.isEmpty() ){
	    System.out.println( "404 NOT FOUND\n" );
	    throw new ResponseStatusException( HttpStatus.NOT_FOUND );
	}
	else{
	    System.out.println( "200 OK\n" );
	    Map<String,Object> user = ls.get(0);

	    String uname = (String) user.get("uname");
	    String pwd = (String) user.get("pwd");

	    Map<String, String> response = new HashMap<>();
	    response.put("uid", String.valueOf(uid) );
	    response.put("uname", uname);
	    response.put("pwd", pwd);
	    return response;
	}
    }

    public void deleteUser( int id ){
	UserService us = context.getBean( UserService.class );

	if( !us.rmUser( id ) ){
	    System.out.println( "404 NOT FOUND\n" );
	    throw new ResponseStatusException( HttpStatus.NOT_FOUND );
	}
	System.out.println( "204 NO CONTENT\n" );
    }

    @GetMapping
    public List<Map<String, String>> hgetAll() {
	System.out.println( "\nGET /hash\n200 OK\n" );
	UserService us = context.getBean( UserService.class );
	List<Map<String,Object>> list = us.listUsers();

	List<Map<String,String>> response = new ArrayList<>();
	for( Map<String,Object> u : list ){
	    String i = String.valueOf( u.get("uid") );
	    String un =  (String) u.get("uname") ;
	    String p =  (String) u.get("pwd") ;
	    Map<String, String> entry = new HashMap<>();
	    entry.put("uid",i);
	    entry.put("uname",un);
	    entry.put("pwd",p);
	    response.add(entry);
	}

        return response;
    }

    @GetMapping("/{uid}")
    public Map<String, String> hget( @PathVariable("uid") int uid ) {
	System.out.println( "\nGET /hash/"+uid );
	return show( uid );
    }
    
    @PostMapping
    @ResponseStatus( HttpStatus.CREATED )
    public Map<String, String> hpost( @RequestBody User u ) {
	System.out.println( "\nPOST /hash" );
	if( u.getUname() == null ||
	    u.getUname().isEmpty() ||
	    u.getUname().contains( " " )
	){
	    System.out.println( "uname missing or contains "
	    + "spaces\n400 BAD REQUEST\n" );
	    throw new ResponseStatusException( HttpStatus.BAD_REQUEST );
	}
	if( u.getPwd() == null ||
	    u.getPwd().isEmpty() ||
	    u.getPwd().contains( " " )
	){
	    System.out.println( "pwd missing or contains "
	    + "spaces\n400 BAD REQUEST\n" );
	    throw new ResponseStatusException( HttpStatus.BAD_REQUEST );
	}

	UserService us = context.getBean( UserService.class );
        User newUser = us.hinsertUser( u );
        Map<String, String> response = new HashMap<>();

	if( newUser == null ){
	    System.out.println( "User exists\n409 CONFLICT\n" );
	    throw new ResourceConflictException( "User exists" );
	}
	else{
	    System.out.println( "Creating resource\n201 CREATED\n" );
	    response.put("uid", String.valueOf( newUser.getUid() ) );
	    response.put("uname", newUser.getUname() );
	    response.put("pwd", newUser.getPwd() );
	}

        return response;
    }

    @PutMapping("/{id}")
    public Map<String, String> hput(
	@PathVariable("id") int id ,
	@RequestBody User u
    ){
	System.out.println( "\nPUT /hash/"+id );
	if( u.getUname() == null ||
	    u.getUname().isEmpty() ||
	    u.getUname().contains( " " )
	){
	    System.out.println( "uname missing or contains "
	    + "spaces\n400 BAD REQUEST\n" );
	    throw new ResponseStatusException( HttpStatus.BAD_REQUEST );
	}
	if( u.getPwd() == null ||
	    u.getPwd().isEmpty() ||
	    u.getPwd().contains( " " )
	){
	    System.out.println( "pwd missing or contains "
	    + "spaces\n400 BAD REQUEST\n" );
	    throw new ResponseStatusException( HttpStatus.BAD_REQUEST );
	}

	UserService us = context.getBean( UserService.class );
	if( us.isDuplicate( id , u ) ){
	    System.out.println( "409 CONFLICT\n" );
	    throw new ResourceConflictException( "User exists" );
	}
        User editedUser = us.hsetUser( id , u );
        Map<String, String> response = new HashMap<>();

	if( editedUser == null ){
	    System.out.println( "404 NOT FOUND\n" );
	    throw new ResponseStatusException( HttpStatus.NOT_FOUND );
	}
	else{
	    System.out.println( "200 OK\n" );
	    response.put("uid", String.valueOf( editedUser.getUid() ) );
	    response.put("uname", editedUser.getUname() );
	    response.put("pwd", editedUser.getPwd() );
	}

        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void hdelete( @PathVariable("id") int id ){
	System.out.println( "\nDELETE /hash/"+id);
	deleteUser( id );
    }

    @PostMapping( "/auth" )
    public void hauthenticate( @RequestBody User u ){
	System.out.println( "\nPOST /hash/auth" );
	if( u.getUname() == null ||
	    u.getUname().isEmpty() ||
	    u.getUname().contains( " " )
	){
	    System.out.println( "uname missing or contains "
	    + "spaces\n400 BAD REQUEST\n" );
	    throw new ResponseStatusException( HttpStatus.BAD_REQUEST );
	}
	if( u.getPwd() == null ||
	    u.getPwd().isEmpty() ||
	    u.getPwd().contains( " " )
	){
	    System.out.println( "pwd missing or contains "
	    + "spaces\n400 BAD REQUEST\n" );
	    throw new ResponseStatusException( HttpStatus.BAD_REQUEST );
	}
	UserService us = context.getBean( UserService.class );

	if( !us.hvalidateUser( u ) ){
	    System.out.println( "404 NOT FOUND\n" );
	    throw new ResponseStatusException( HttpStatus.NOT_FOUND );
	}
	System.out.println( "200 OK\n" );
    }
}
