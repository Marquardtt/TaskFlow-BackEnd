package br.demo.backend.model.enums;

public enum TypeOfNotification {

    CHANGETASK,
    ADDINGROUP,

    REMOVEINGROUP,
    CHANGEPERMISSION,
    //when pass a specific number of points (1000, 5000, etc.)
    POINTS,
    //when i schedule a task or project
    SCHEDULE,
    //when a project or task deadline is near
    DEADLINE,
    //when i receive a message
    CHAT,
    //when ia task than i am responsible receive a comment
    COMMENTS,
    INVITETOPROJECT
}
