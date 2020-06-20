package controller;

public class InvalidException extends Exception{
	public InvalidException(){
		
	}
	public InvalidException(String msg){
		super(msg);
	}
}
