package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class PmMsg extends Message {
    private String userName;
    private String content;
    private int opCode;

    public PmMsg(String userName, String content, int opCode){
        this.content = content;
        this.userName = userName;
        this.opCode = opCode;
    }
}
