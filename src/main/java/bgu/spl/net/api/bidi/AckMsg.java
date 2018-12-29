package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class AckMsg extends Message {
    private int opcode;
    private int msgRelatedOpcode;

    // add more things for each kind of message, the "optional" field//

    public AckMsg(int thisOpcode, int msgRelatedOpcode){
        this.opcode = thisOpcode;
        this.msgRelatedOpcode = msgRelatedOpcode;
    }
}
