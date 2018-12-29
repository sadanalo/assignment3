package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class PostMsg extends Message {
    private String content;

   public PostMsg(String content, int opCode){
        super(opCode);
        this.content = content;
    }
}
