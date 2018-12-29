package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class PostMsg extends Message {
    private String content;
    private int opCode;

   public PostMsg(String content, int opCode){

        this.content = content;
        this.opCode= opCode;
    }
}
