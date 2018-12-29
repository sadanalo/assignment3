package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class StatMsg extends Message{
    private String userName;

    public StatMsg(String userName, int opCode){
       super(opCode);
        this.userName = userName;
    }
}
