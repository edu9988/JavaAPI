package com.project.nameless.controller;
import com.project.nameless.model.User;
import com.project.nameless.model.UserService;

import java.util.List;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired private ApplicationContext context;

    @GetMapping("/users")
    public List<Map<String, String>> getAll() {
	System.out.println( "\nGET /users\n" );
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

    @GetMapping("/users/{uid}")
    public Map<String, String> get( @PathVariable("uid") int uid ) {
	System.out.println( "\nGET /users/"+uid );
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

    @PostMapping("/users")
    public Map<String, String> post( @Valid @RequestBody User u ) {
	System.out.println( "\nPOST /users\n" );
	UserService us = context.getBean( UserService.class );
        int id = us.insertUser( u );
        Map<String, String> response = new HashMap<>();

	if( id == -1 ){
	    response.put("err", "user exists" );
	}
	else{
	    response.put("uid", String.valueOf(id) );
	    response.put("uname", u.getUname() );
	    response.put("pwd", u.getPwd() );
	}

        return response;
    }

    @PutMapping("/users/{id}")
    public Map<String, String> put(
	@PathVariable("id") int id ,
	@Valid @RequestBody User u
    ){
	System.out.println( "\nPUT /users/"+id+"\n" );
	UserService us = context.getBean( UserService.class );
        Map<String, String> response = new HashMap<>();

	if( us.setUser( id ,u ) ){
	    response.put("uid", String.valueOf(id) );
	    response.put("uname", u.getUname() );
	    response.put("pwd", u.getPwd() );
	}
	else{
	    response.put("err", "user exists" );
	}

        return response;
    }

    @DeleteMapping("/users/{id}")
    public Map<String, String> delete( @PathVariable("id") int id ){
	System.out.println( "\nDELETE /users/"+id+"\n" );
	UserService us = context.getBean( UserService.class );
        Map<String, String> response = new HashMap<>();

	if( us.rmUser( id ) ){
	    response.put("uid", String.valueOf(id) );
	}
	else{
	    response.put( "err" , "user does not exist" );
	}

        return response;
    }
}
