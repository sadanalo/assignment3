package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.User;

import java.util.concurrent.ConcurrentHashMap;

public class DataBase {

   private ConcurrentHashMap<String,User> usersByName;
    private ConcurrentHashMap<String ,User> loggedUsers;
    private ConcurrentHashMap<Integer,User> usersByConnectionId ;




    private static class dataBaseHolder{
        private static DataBase instance = new DataBase();
    }
    private DataBase (){
        this.usersByName = new ConcurrentHashMap<>();
        this.loggedUsers = new ConcurrentHashMap<>();
        this.usersByConnectionId = new ConcurrentHashMap<>();
    }
    public static DataBase getInstance(){
        return dataBaseHolder.instance;
    }




    public ConcurrentHashMap<String,User> getUsersByName() {
        return usersByName;
    }

    public void registerUser (User user){
        this.usersByName.put(user.getName(),user);
    }
    public void logInUser (User user){
        this.loggedUsers.put(user.getName(), user);
    }
    public ConcurrentHashMap<String, User> getLoggedUsers() {
        return loggedUsers;
    }
    public ConcurrentHashMap<Integer, User> getUsersByConnectionId() {
        return usersByConnectionId;
    }

}
