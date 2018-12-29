package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class LoginMsg extends Message {
    private int opCode;
    public LoginMsg(int opCode){
        this.opCode = opCode;
    }

}
