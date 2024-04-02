package br.demo.backend.model.enums;


public enum TypePermission {
    UPDATE ("PUT"),
    DELETE("DELETE"),
    CREATE("POST"),
    UPDATE_DELETE("PUT_PATCH_DELETE"),
    UPDATE_CREATE("PUT_PATCH_POST"),
    DELETE_CREATE("DELETE_POST"),
    UPDATE_DELETE_CREATE("PUT_DELETE_POST"),
    READ("GET");
    String method;

    TypePermission(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
