package br.demo.backend.interfaces;


public interface IValidationId <T , ID>{

    void ofObject(ID id, T obj);
    void of(ID id, ID id2);
}
