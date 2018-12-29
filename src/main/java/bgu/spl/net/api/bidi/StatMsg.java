package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class StatMsg extends Message{
    private String userName;
    private int opCode;

    public StatMsg(String userName, int opCode){
        this.userName = userName;
        this.opCode = opCode;
    }
}
