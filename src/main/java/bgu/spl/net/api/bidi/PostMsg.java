package bgu.spl.net.api.bidi;

import java.util.LinkedList;

public class PostMsg extends Message {
    private String content;
    private LinkedList<String> taggedUsers;

   public PostMsg(String content){
        super((short)5);
        this.content = content;
        this.taggedUsers = findTaggedUsers(content);

   }

    private LinkedList<String> findTaggedUsers(String content) {
       LinkedList<String> taggedUsers = new LinkedList<>();
       // TODO parse the content and find tagged users//
return taggedUsers;
    }

    public LinkedList<String> getTaggedUsers() {
        return taggedUsers;
    }

    public String getContent() {
        return content;
    }
}
