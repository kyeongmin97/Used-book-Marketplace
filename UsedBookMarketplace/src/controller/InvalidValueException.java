package controller;

public class InvalidValueException extends Exception{
	public InvalidValueException(){
		
	}
	public InvalidValueException(String msg){
		super(msg);
	}
}
