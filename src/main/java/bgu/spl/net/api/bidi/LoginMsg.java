package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class LoginMsg extends Message {
private String userName;
private String password;
    public LoginMsg(short opCode, String userName, String password){

        super(opCode);
        this.userName= userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
