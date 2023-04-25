package model;

public class Utilisateur {
    private String username;
    private String password;
    public enum Status {ONLINE,OFFLINE,AWAY}
    public enum UserType {CLASSIC,MODERATOR,ADMINISTRATOR}
    private Status status;
    private UserType userType;
    private boolean isBan;

    Utilisateur(String username,String mdp){
        this.username=username;
        this.password=mdp;
        this.isBan = false;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setStatut(Status status){
        this.status=status;
    }

    public Status getStatus() {
        return status;
    }

    public Utilisateur.UserType getUserType() {
        return userType;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBan(boolean ban) {
        this.isBan = ban;
    }
    public boolean getBan() {
        return isBan;
    }
}
