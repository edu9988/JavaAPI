package com.project.nameless.model;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService{
    
    @Autowired UserDAO udao;
    
    public int insertUser( User u ){
	return udao.insertUser( u );
    }

    public boolean validateUser( User u ){
	return udao.validateUser( u );
    }

    public List<Map<String,Object>> listUsers(){
	return udao.listUsers();
    }

    public List<Map<String,Object>> getUser( int id ){
	return udao.getUser( id );
    }

    public boolean setUser( int uid , User u ){
	return udao.setUser( uid , u );
    }

    public boolean isDuplicate( int id , User u ){
	return udao.isDuplicate( id , u );
    }

    public boolean rmUser( int uid ){
	return udao.rmUser( uid );
    }
    
    public User hinsertUser( User u ){
	return udao.hinsertUser( u );
    }

    public boolean hvalidateUser( User u ){
	return udao.hvalidateUser( u );
    }

    public User hsetUser( int uid , User u ){
	return udao.hsetUser( uid , u );
    }
}
