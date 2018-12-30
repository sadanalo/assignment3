package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.User;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DataBase {

   private ConcurrentHashMap<String,User> usersByName;
    private ConcurrentHashMap<Integer,User> usersByConnectionId;





    private static class dataBaseHolder{
        private static DataBase instance = new DataBase();
    }
    private DataBase (){
        this.usersByName = new ConcurrentHashMap<>();
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

    public void logInUser (User user, int connectionId){
        this.usersByConnectionId.put(connectionId, user);
        user.setLogged(true);
    }
    public void logoutUser(int connectionId){
       this.usersByConnectionId.get(connectionId).setLogged(false);
        this.usersByConnectionId.remove(connectionId,this.usersByConnectionId.get(connectionId) );
    }
    public ConcurrentHashMap<Integer, User> getUsersByConnectionId() {
        return usersByConnectionId;
    }

}
