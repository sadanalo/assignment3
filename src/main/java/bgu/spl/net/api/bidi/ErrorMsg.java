package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class ErrorMsg extends Message  {
    private int msgRelatedOpcode;

    public ErrorMsg(short msgRelatedOpcode){

        super((short) 11);

        this.msgRelatedOpcode = msgRelatedOpcode;
    }

}
