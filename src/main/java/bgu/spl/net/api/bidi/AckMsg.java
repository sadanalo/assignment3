package bgu.spl.net.api.bidi;

public class AckMsg implements Message {
    private int thisOpcode;
    private int msgRelatedOpcode;

    // add more things for each kind of message//

    public AckMsg(int thisOpcode, int msgRelatedOpcode){
        this.thisOpcode = thisOpcode;
        this.msgRelatedOpcode = msgRelatedOpcode;
    }
}
