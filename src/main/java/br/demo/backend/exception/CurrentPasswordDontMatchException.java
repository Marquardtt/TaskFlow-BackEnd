package br.demo.backend.exception;

public class CurrentPasswordDontMatchException extends RuntimeException{
    public CurrentPasswordDontMatchException(){super(("The password you have passed doens't match your current password"));}
}
