package bgu.spl.net.api.bidi;

public class FollowAckMsg extends AckMsg {

    private int numOfusers;
    private boolean followUnfollow;
    private String userNameList;

    public FollowAckMsg(short msgRelatedOpcode, int numOfusers,  String userNameList ) {
        super(msgRelatedOpcode);
        this.numOfusers = numOfusers;
        this.userNameList = userNameList;

    }

}
