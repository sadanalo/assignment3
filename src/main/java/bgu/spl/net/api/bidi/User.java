package bgu.spl.net.api.bidi;

import java.util.concurrent.ConcurrentHashMap;

public class User {

    private String name;
    private String password;
    private ConcurrentHashMap<String, User> followingList;


    public User (String name, String password ){
        this.name = name;
        this.password = password;
        this.followingList = new ConcurrentHashMap<>();

    }

    public String getName() {
        return name;
    }


    public String getPassword() {
        return password;
    }

    public ConcurrentHashMap<String, User> getFollowingList() {
        return followingList;
    }
}
