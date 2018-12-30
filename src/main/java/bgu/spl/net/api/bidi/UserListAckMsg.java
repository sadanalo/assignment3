package bgu.spl.net.api.bidi;

import java.util.Set;

public class UserListAckMsg extends AckMsg {
    private short numOfusers;
    private String usersList;

    public UserListAckMsg(short msgRelatedOpcode, short numOfUsers, Set<String> users) {
        super(msgRelatedOpcode);
        this.numOfusers = numOfUsers;
        this.usersList = getUsersList(users);

    }

    private String getUsersList(Set<String> users) {
        String list = "";
        for (String user : users){
             list +=  "\0" + user;
        }
        return list;
    }
}
