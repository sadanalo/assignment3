package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class RegisterMsg extends Message{

    private String userName;
    private String password;
    private int opCode;

    public RegisterMsg(int opCode, String userName, String password){
        this.userName = userName;
        this.password = password;
        this.opCode = opCode;
    }
}
