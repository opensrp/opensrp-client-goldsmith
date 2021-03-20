package org.smartregister.goldsmith.domain;

public class ChwPerson {

    private String identifier;
    private boolean isActive;
    private String fullName;
    private String userId;
    private String userName;

    public ChwPerson(String identifier, boolean isActive, String fullName, String userId, String userName) {
        this.identifier = identifier;
        this.isActive = isActive;
        this.fullName = fullName;
        this.userId = userId;
        this.userName = userName;
    }

    public ChwPerson() {
    }


    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
