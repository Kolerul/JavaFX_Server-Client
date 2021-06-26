package mainPart;

import java.io.Serializable;

public class UserData implements Serializable{
    private String login;
    private String password;
    private Serializable data;
    private boolean itsExistedAccount = true;

    public UserData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserData(String login, String password, boolean itsExistedAccount) {
        this.login = login;
        this.password = password;
        this.itsExistedAccount = itsExistedAccount;
    }

    public void setData(Serializable data) {
        this.data = data;
    }
    public Serializable getData() {return data;}
    public boolean getExistedAccount() {return itsExistedAccount;}
    public String getLogin() {return login;}
    public void setLogin(String login) {this.login = login;}
    public String getPassword() {return password;}
}
