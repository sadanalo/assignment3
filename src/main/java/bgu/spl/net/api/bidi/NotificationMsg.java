package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class NotificationMsg extends Message {
    private boolean privateMessage;
    private String postingUser;
    private String content;
    private char type;

    public NotificationMsg( String postingUser, String content , char type){
        super((short) 9);
        this.postingUser = postingUser;
        this.content = content;
        this.type = type;
    }

}
