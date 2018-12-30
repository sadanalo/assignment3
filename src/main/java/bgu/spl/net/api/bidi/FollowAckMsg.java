package bgu.spl.net.api.bidi;

public class FollowAckMsg extends Message {

    private short numOfusers;
    private boolean followUnfollow;
    private String userNameList;

    public short getNumOfusers() {
        return numOfusers;
    }

    public boolean isFollowUnfollow() {
        return followUnfollow;
    }

    public String getUserNameList() {
        return userNameList;
    }

    public short getMsgRelatedOpcode() {
        return msgRelatedOpcode;
    }

    private short msgRelatedOpcode;

    public FollowAckMsg(short msgRelatedOpcode, short numOfusers,  String userNameList ) {
        super((short)10);
        this.numOfusers = numOfusers;
        this.userNameList = userNameList;
        this.msgRelatedOpcode = msgRelatedOpcode;

    }

}
