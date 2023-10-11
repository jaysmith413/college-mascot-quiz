package Exceptions;

public class InvalidInput extends RuntimeException{
    public InvalidInput(){
        super("Invalid Input. Please enter a number (1-4).");
    }
}
