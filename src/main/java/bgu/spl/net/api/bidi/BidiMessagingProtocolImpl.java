package bgu.spl.net.api.bidi;
import bgu.spl.net.srv.DataBase;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {


    private Connections<Message> connections;
    private boolean shouldTerminate;
    private int connectionId;


    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.connections = connections;
        this.connectionId = connectionId;

    }

    @Override
    public void process(Message message) {
        switch (message.getOpCode()) {
            case 1:
                if (message instanceof RegisterMsg) {
                    processRegister((RegisterMsg) message);
                    break;
                }
            case 2:
                if (message instanceof LoginMsg){
                    processLogin ((LoginMsg) message);
                    break;
                }
            case 3:
                if (message instanceof LogoutMsg){
                    processLogout((LogoutMsg) message);
                    break;
                }
            case 4:
                if (message instanceof FollowMsg){
                    processFollow((FollowMsg) message);
                }

        }
    }

    private void processFollow(FollowMsg message) {
        int successful = 0;
        String userNameList = "";
        //if current user is logged//
        if(DataBase.getInstance().getLoggedUsers().containsKey(DataBase.getInstance().getUsersByConnectionId().get(connectionId).getName())) {
            if (message.isFollow()) {
                for (String userName : message.getUserNameList()) {
                    // if current user is not following userName already//
                    if (!DataBase.getInstance().getUsersByConnectionId().get(connectionId).getFollowingList().containsKey(userName)) {
                        //add userName to current user followingList//
                        DataBase.getInstance().getUsersByConnectionId().get(connectionId).getFollowingList().put(userName, DataBase.getInstance().getUsersByName().get(userName));
                        userNameList += userName;
                        ++ successful;
                    }
                }
                if (successful != 0){
                    connections.send(connectionId, new FollowAckMsg((short) 4, successful, userNameList));
                }
                else{ // successful is 0//
                    connections.send(connectionId, new ErrorMsg((short) 4));
                }
            }else{  // message is unFollow//
                for (String userName : message.getUserNameList()) {
                    // if current user is not following userName already//
                    if (DataBase.getInstance().getUsersByConnectionId().get(connectionId).getFollowingList().containsKey(userName)) {
                        //add userName to current user followingList//
                        DataBase.getInstance().getUsersByConnectionId().get(connectionId).getFollowingList().remove(userName, DataBase.getInstance().getUsersByName().get(userName));
                        successful++;
                    }
                }
                if (successful != 0){
                    connections.send(connectionId, new FollowAckMsg((short) 4, successful, userNameList));
                }
                else{ // successful is 0//
                    connections.send(connectionId, new ErrorMsg((short) 4));
                }

            }

        }
        else {  //current user is not logged//
            connections.send(connectionId, new ErrorMsg((short) 4));
        }


    }

    private void processLogout(LogoutMsg message) {
        //if user is logged in//
        if(DataBase.getInstance().getLoggedUsers().containsKey(DataBase.getInstance().getUsersByConnectionId().get(connectionId).getName())){
            //take out from loggedUsers//
            DataBase.getInstance().getLoggedUsers().remove(DataBase.getInstance().getUsersByConnectionId().get(connectionId).getName());
            DataBase.getInstance().getUsersByConnectionId().remove(connectionId);
            connections.send(connectionId, new AckMsg((short)3));
        }
        else {
            connections.send(connectionId, new ErrorMsg((short) 3));
        }

    }

    private void processLogin(LoginMsg message) {
        for (User user : DataBase.getInstance().getUsersByName().values()){
            //compare names//
            if (message.getUserName().equals(user.getName())){
                //compare passwords//
                if(message.getPassword().equals(user.getPassword())){
                    //check if already logged//
                    if(!DataBase.getInstance().getLoggedUsers().containsKey(user.getName())){
                        //login the user
                        DataBase.getInstance().logInUser(user);
                        connections.send(connectionId, new AckMsg((short)2));
                    }

                }
            }
            else {
                connections.send(connectionId, new ErrorMsg((short) 2));
            }
        }
    }

    private void processRegister(RegisterMsg message) {
        for (String userName : DataBase.getInstance().getUsersByName().keySet()){
            if(userName.equals(message.getUserName())){
                connections.send(connectionId, new ErrorMsg((short)1));
                return;
            }
        }
        User user = new User (message.getUserName(), message.getPassword());
        DataBase.getInstance().registerUser(user);
        connections.send(connectionId, new AckMsg((short)1));
    }



    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }
}
