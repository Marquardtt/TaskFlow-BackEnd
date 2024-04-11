package br.demo.backend.utils;

public interface ModelToGetDTO <T, S>{

    S tranform(T t);
}
