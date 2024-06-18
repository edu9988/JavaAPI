package com.project.nameless.model;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

import jakarta.annotation.PostConstruct;

import java.sql.Types;

@Repository
public class UserDAO{
    
    @Autowired DataSource dataSource;
    
    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize(){
	jdbc = new JdbcTemplate( dataSource );
    }

    public int insertUser( User u ){
	String sql = "SELECT EXISTS( SELECT * FROM tb_user " +
	    "WHERE uname = ?)";

	Object[] obj = new Object[1];
	obj[0] = u.getUname();

	int[] types = new int[1];
	types[0] = Types.VARCHAR;

	Boolean ansBool = jdbc.queryForObject(sql,obj,types,(rs,rowNum) -> 
	    Boolean.valueOf( rs.getBoolean("exists")));

	if( ansBool ){
	    System.out.println( "User exists\n" );
	    return -1;
	}

	sql = "INSERT INTO tb_user (uname,pwd) " +
	    "VALUES (?,?)";

	obj = new Object[2];
	obj[0] = u.getUname();
	obj[1] = u.getPwd();

	if( jdbc.update(sql,obj) == 1 ){
	    sql = "SELECT uid FROM tb_user " + 
		"WHERE uname = ?";

	    obj = new Object[1];
	    obj[0] = u.getUname();

	    int ansInt = jdbc.queryForObject( sql , obj , types , (rs,rowNum) -> 
		Integer.valueOf( rs.getInt("uid") ) );

	    return ansInt;
	}
	else
	    return -1;
    }

    public boolean validateUser( User u ){
	String sql = "SELECT EXISTS( SELECT * FROM tb_user " +
	    "WHERE uname = ? AND pwd = ?)";

	Object[] obj = new Object[2];
	obj[0] = u.getUname();
	obj[1] = u.getPwd();

	int[] types = new int[2];
	types[0] = Types.VARCHAR;
	types[1] = Types.VARCHAR;

	return jdbc.queryForObject(sql,obj,types,(rs,rowNum) -> 
	    Boolean.valueOf( rs.getBoolean("exists") )
	);
    }

    public List<Map<String, Object>> listUsers() {
	String sql = "SELECT * FROM tb_user";
	return jdbc.queryForList( sql );
    }

    public List<Map<String,Object>> getUser( int id ){
	String sql = "SELECT * FROM tb_user WHERE uid = ?";
	Object[] obj = new Object[1];
	obj[0] = id;
	return jdbc.queryForList( sql , obj );
    }

    public boolean isDuplicate( int uid , User u ){
	String sql = "SELECT EXISTS( SELECT * FROM tb_user "
	    + "WHERE uname = ? AND uid != ? )";

	Object[] obj = new Object[2];
	obj[0] = u.getUname();
	obj[1] = uid;

	int[] types = new int[2];
	types[0] = Types.VARCHAR;
	types[1] = Types.INTEGER;

	Boolean ans = jdbc.queryForObject(sql,obj,types,(rs,rowNum) -> 
	    Boolean.valueOf( rs.getBoolean("exists")));

	if( ans ){
	    System.out.println( "DAO: User exists" );
	    return true;
	}
	else
	    return false;
    }

    public boolean setUser( int uid , User u ){
	String sql = "UPDATE tb_user SET uname = ?,"
	    + "pwd = ? WHERE uid = ?";
	Object[] obj = new Object[3];
	obj[0] = u.getUname();
	obj[1] = u.getPwd();
	obj[2] = uid;
	if( jdbc.update( sql , obj ) == 1 )
	    return true;
	else
	    return false;
    }

    public boolean rmUser( int uid ){
	String sql = "DELETE FROM tb_user WHERE uid = ?";
	Object[] obj = new Object[1];
	obj[0] = uid;
	if( jdbc.update( sql , obj ) == 1 )
	    return true;
	else
	    return false;
    }


    public User hinsertUser( User u ){
	String sql = "SELECT EXISTS( SELECT * FROM tb_user " +
	    "WHERE uname = ?)";

	Object[] obj = new Object[1];
	obj[0] = u.getUname();

	int[] types = new int[1];
	types[0] = Types.VARCHAR;

	Boolean ans = jdbc.queryForObject(sql,obj,types,(rs,rowNum) -> 
	    Boolean.valueOf( rs.getBoolean("exists")));

	if( ans ){
	    System.out.println( "DAO: User exists\n" );
	    return null;
	}

	sql = "INSERT INTO tb_user (uname,pwd) " +
	    "VALUES (?,crypt(?,gen_salt('bf',8)))";

	obj = new Object[2];
	obj[0] = u.getUname();
	obj[1] = u.getPwd();

	try{
	    jdbc.update(sql,obj);
	}
	catch( Exception e ){
	    System.out.println( "\nUserDAO.hinsertUser():"
		+ "Insert query Exception:"
		+ e + "\n"
	    );
	    return null;
	}

	sql = "SELECT * FROM tb_user " +
	    "WHERE uname = ?";

	obj = new Object[1];
	obj[0] = u.getUname();

	User newUser = jdbc.queryForObject(
	    sql , obj , types , (rs,rowNum) -> {
		User ret = new User();
		ret.setUid( rs.getInt( "uid" ) );
		ret.setUname( rs.getString( "uname" ) );
		ret.setPwd( rs.getString( "pwd" ) );
		return ret;
	    }
	);
	return newUser;
    }

    public boolean hvalidateUser( User u ){
	String sql = "SELECT EXISTS( SELECT * FROM tb_user " +
	    "WHERE uname = ? AND pwd = crypt(?,pwd))";

	Object[] obj = new Object[2];
	obj[0] = u.getUname();
	obj[1] = u.getPwd();

	int[] types = new int[2];
	types[0] = Types.VARCHAR;
	types[1] = Types.VARCHAR;

	return jdbc.queryForObject(sql,obj,types,(rs,rowNum) -> 
	    Boolean.valueOf( rs.getBoolean("exists") )
	);
    }

    public User hsetUser( int uid , User u ){
	String sql = "UPDATE tb_user SET "
	    + "uname = ? , pwd = "
	    + "crypt(?,gen_salt('bf',8)) "
	    + "WHERE uid = ?";
	Object[] obj = new Object[3];
	obj[0] = u.getUname();
	obj[1] = u.getPwd();
	obj[2] = uid;
	if( jdbc.update( sql , obj ) != 1 )
	    return null;

	sql = "SELECT * FROM tb_user " +
	    "WHERE uid = ?";

	obj = new Object[1];
	obj[0] = uid;

	int[] types = new int[1];
	types[0] = Types.INTEGER;

	User newUser = jdbc.queryForObject(
	    sql , obj , types , (rs,rowNum) -> {
		User ret = new User();
		ret.setUid( rs.getInt( "uid" ) );
		ret.setUname( rs.getString( "uname" ) );
		ret.setPwd( rs.getString( "pwd" ) );
		return ret;
	    }
	);
	return newUser;
    }
}
