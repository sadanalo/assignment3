package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class NotificationMsg extends Message {
    private boolean privateMessage;
    private String postingUser;
    private String content;

    public NotificationMsg(int privateMessage, String postingUser, String content, int opCode){
        super(opCode);
        this.privateMessage = (privateMessage == 0);
        this.postingUser = postingUser;
        this.content = content;
    }

}
