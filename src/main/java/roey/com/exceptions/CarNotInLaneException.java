package roey.com.exceptions;

public class CarNotInLaneException extends RuntimeException{
    public CarNotInLaneException(String message) {
        super(message);
    }
}
