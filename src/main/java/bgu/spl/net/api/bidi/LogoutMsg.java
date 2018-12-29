package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class LogoutMsg extends Message {
    private int opCode;

    public LogoutMsg (int opCode){
        this.opCode = opCode;
    }
}
