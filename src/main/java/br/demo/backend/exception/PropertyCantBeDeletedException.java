package br.demo.backend.exception;

public class PropertyCantBeDeletedException extends RuntimeException {
    public PropertyCantBeDeletedException() {
        super("This property can't be deleted because it is a prop ordering in some page.");
    }
}
