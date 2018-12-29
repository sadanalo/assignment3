package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class ErrorMsg extends Message  {
    private int opCode;
    private int msgRelatedOpcode;

    public ErrorMsg(int thisOpCode, int msgRelatedOpcode){
        this.msgRelatedOpcode = msgRelatedOpcode;
        this.opCode = thisOpCode;
    }

}
