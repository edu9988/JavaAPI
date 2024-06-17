package com.project.nameless.model;

//POJO - Plain Old Java Object
public class User{

    private int uid;
    private String uname;
    private String pwd;

    //Sobrecarga
    public User(){
    }

    public User( String uname , String pwd ){
	this.uname = uname;
	this.pwd = pwd;
    }

    public User( int uid , String uname , String pwd ){
	this.uid = uid;
	this.uname = uname;
	this.pwd = pwd;
    }

    public void setUid( int uid ){
	this.uid = uid;
    }

    public void setUname( String uname ){
	this.uname = uname;
    }

    public void setPwd( String pwd ){
	this.pwd = pwd;
    }

    public int getUid(){
	return uid;
    }

    public String getUname(){
	return uname;
    }

    public String getPwd(){
	return pwd;
    }
}
