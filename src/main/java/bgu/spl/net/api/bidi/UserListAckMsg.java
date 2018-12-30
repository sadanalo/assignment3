package bgu.spl.net.api.bidi;

import java.util.Set;

public class UserListAckMsg extends Message {
    private short numOfusers;
    private String usersList;

    public short getMsgRelatedOpcode() {
        return msgRelatedOpcode;
    }

    protected short msgRelatedOpcode;


    public UserListAckMsg(short msgRelatedOpcode, short numOfUsers, Set<String> users) {
        super((short)10);
        this.numOfusers = numOfUsers;
        this.usersList = getUsersList(users);
        this.msgRelatedOpcode = msgRelatedOpcode;


    }

    private String getUsersList(Set<String> users) {
        String list = "";
        for (String user : users){
             list +=  "\0" + user;
        }
        return list;
    }

    public String getUsersList() {
        return usersList;
    }

    public short getNumOfusers() {
        return numOfusers;
    }
}
