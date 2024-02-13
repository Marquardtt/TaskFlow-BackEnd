package br.demo.backend.model.enums;


public enum TypeOfProperty {

//  Select
    RADIO,
//  Select
    SELECT,
//  Select
    TAG,
//  Limited
    TEXT,
//  Date
    DATE,
//  Limited
    TIME,
//  Limited
    USER,
//  Limited
    PROGRESS,
//  Limited
    ARCHIVE,
//  Select
    CHECKBOX,
//  Limited
    NUMBER,
    //Os dois proximos são para atender o requisito de dependencia entre tarefas e projetos
    //Limited
    TASK,
    //  Limited
    PROJECT
}
