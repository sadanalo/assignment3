package bgu.spl.net.api.bidi;

import java.util.LinkedList;

public class FollowMsg implements Message {

    private boolean follow;
    private int numOfUsers;
    private LinkedList<String> userNameList;
    private int opCode;

    public FollowMsg(int follow, int numOfUsers, LinkedList<String> userNameList, int opCode) {
        this.follow = (follow == 0);
        this.numOfUsers = userNameList.size();
        this.opCode = opCode;
        for (String userName : userNameList) {
            this.userNameList.add(userName);

        }

    }
}
