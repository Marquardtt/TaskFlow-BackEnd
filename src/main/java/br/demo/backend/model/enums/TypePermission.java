package br.demo.backend.model.enums;


public enum TypePermission {
    UPDATE ("PUT_GET"),
    DELETE("DELETE_GET"),
    CREATE("POST_GET"),
    UPDATE_DELETE("PUT_PATCH_DELETE_GET"),
    UPDATE_CREATE("PUT_PATCH_POST_GET"),
    DELETE_CREATE("DELETE_POST_GET"),
    UPDATE_DELETE_CREATE("PUT_DELETE_POST_GET"),
    READ("GET");
    String method;

    TypePermission(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
