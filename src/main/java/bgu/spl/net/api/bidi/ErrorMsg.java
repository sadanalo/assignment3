package bgu.spl.net.api.bidi;

public class ErrorMsg implements Message {
    private int thisOpCode;
    private int msgRelatedOpcode;

    public ErrorMsg(int thisOpCode, int msgRelatedOpcode){
        this.msgRelatedOpcode = msgRelatedOpcode;
        this.thisOpCode = thisOpCode;
    }

}
