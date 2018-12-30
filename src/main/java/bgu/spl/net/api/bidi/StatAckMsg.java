package bgu.spl.net.api.bidi;

public class StatAckMsg extends AckMsg {
    private short numOfPosts;
    private short numOfFollowers;
    private short numOfFollowing;

    public StatAckMsg(short msgRelatedOpcode , short numOfPosts, short numOfFollowers, short numOfFollowing) {
        super(msgRelatedOpcode);
        this.numOfFollowers= numOfFollowers;
        this.numOfFollowing = numOfFollowing;
        this.numOfPosts = numOfPosts;

    }
}
