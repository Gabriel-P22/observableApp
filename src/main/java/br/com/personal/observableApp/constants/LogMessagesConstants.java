package br.com.personal.observableApp.constants;

public enum LogMessagesConstants {
    ERROR_COUNT("Error on user job..."),
    USER_CREATED_COUNT("User created!"),
    USER_UPDATED_COUNT("User updated!"),
    USER_DELETED_COUNT("User deleted!"),
    USER_FIND_COUNT("User find!");

    private final String value;

    LogMessagesConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
