package bgu.spl.net.api.bidi;

public class FollowAckMsg extends AckMsg {
private int numOfusers;
private String userNameList;
    public FollowAckMsg(short msgRelatedOpcode, int numOfusers,  String userName ) {
        super(msgRelatedOpcode);


    }

}
