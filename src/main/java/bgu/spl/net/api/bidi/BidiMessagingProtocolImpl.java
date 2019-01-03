package bgu.spl.net.api.bidi;
import bgu.spl.net.srv.DataBase;
public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {


    private Connections<Message> connections;
    private boolean shouldTerminate;
    private int connectionId;
    private DataBase dataBase;

    public BidiMessagingProtocolImpl(DataBase dataBase) {
        this.dataBase = dataBase;
    }


    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.connections = connections;
        this.connectionId = connectionId;

    }

    @Override
    public void process(Message message) {
        switch (message.getOpCode()) {
            case 1:
                processRegister((RegisterMsg) message);
                break;

            case 2:
                processLogin ((LoginMsg) message);
                break;
            case 3:
                processLogout((LogoutMsg) message);
                break;
            case 4:
                processFollow((FollowMsg) message);
                break;
            case 5:
                processPost((PostMsg)message);
                break;
            case 6:
                processPm((PmMsg) message);
                break;
            case 7:
                processUserList((UserListMsg) message);
                break;
            case 8:
                processStatMsg((StatMsg)message);
                break;


        }
    }

    private void processStatMsg(StatMsg message) {
        // current user is not logged or he is not registered//
        if (!currentUser().isLogged() || dataBase.getUsersByName().containsKey(currentUser().getName())){
            connections.send(connectionId ,new  ErrorMsg( message.getOpCode()));
        }
        else{
            connections.send(connectionId, new StatAckMsg (message.getOpCode(),
                    (short) currentUser().getNumOfPosts(), (short)currentUser().getFollowersList().size(),
                    (short)currentUser().getFollowingList().size())
            );
        }
    }

    private void processUserList(UserListMsg message) {
        if (!currentUser().isLogged()){
            connections.send(connectionId, new ErrorMsg(message.getOpCode()));
        }
        else{  //making the list to string is in userListAckMsg//
            connections.send(connectionId, new UserListAckMsg(message.getOpCode(),(short) dataBase.getUsersByName().size(), dataBase.getRegistrationQueue()));
        }
    }

    private void processPm(PmMsg message) {
        byte type = 0;   //this is how we declare byte?
        NotificationMsg notificationMsg = new NotificationMsg(currentUser().getName(), message.getContent(),type);

        //if current user is not logged or the user receiving the message is not registered//
        if(!currentUser().isLogged() || getUser(message.getUserName()) == null){
            connections.send(connectionId, new ErrorMsg((short)message.getOpCode()));
        }else{
            if (getUser(message.getUserName()).isLogged()){

                //send notification//
                connections.send(getUserConnectionId(message.getUserName()),notificationMsg);
                // add to sending user sentMessageList//
                currentUser().getSentMessages().add(message);
            }
            else{ //user receiving the message is not logged

                //add notification  to the receiving user NotReadMessageQueue, maybe just notification?//
                getUser(message.getUserName()).getNotReadMessageQueue().add(notificationMsg);
                // add to sending user sentMessageList//
                currentUser().getSentMessages().add(message);
            }
        }
    }
    // at this moment we are sending posts and notification, perhaps we need only notification//
    private void processPost(PostMsg message) {
        //if user is not logged//
        byte type = 1;
        NotificationMsg notificationMsg = new NotificationMsg(currentUser().getName(), message.getContent(),type);
        if(!currentUser().isLogged()){
            connections.send(connectionId, new ErrorMsg((short) message.getOpCode()));
        }
        else {
            for(String taggedUser : message.getTaggedUsers()) {  //tagged users //
                if (!currentUser().getFollowersList().containsKey(taggedUser)){  //maybe taggedUser is also on the followers list//
                    if (getUser(taggedUser).isLogged()) {

                        //send notification//
                        connections.send(getUserConnectionId(taggedUser), notificationMsg);
                        // add message to posting user list//
                        currentUser().getSentMessages().add(message);
                    } else { // the user is registered but not logged ,//
                        // so we add the notification to his notReadMessageQueue,//
                        // and he will receive it  after login//

                        // add message and notification to the tagged user NotReadMessageQueue
                        getUser(taggedUser).getNotReadMessageQueue().add(notificationMsg);
                        // add the post the posting user sentMessage list//
                        currentUser().getSentMessages().add(message);
                    }
                }
            }
            for (String userName : currentUser().getFollowersList().keySet()){  // followers//
                if(getUser(userName).isLogged()){
                    connections.send(getUserConnectionId(userName), message);
                    connections.send(getUserConnectionId(userName), notificationMsg);
                    currentUser().getSentMessages().add(message);
                } else{
                    getUser(userName).getNotReadMessageQueue().add(notificationMsg);
                    getUser(userName).getNotReadMessageQueue().add(message);
                    currentUser().getSentMessages().add(message);
                }
            }
        }
    }

    private void processFollow(FollowMsg message) {
        short successful = 0;  //maybe atomicInt? synchronized? something?//
        String userNameList = "";
        //if current user is logged//
        if(currentUser().isLogged()) {
            if (message.isFollow()) {
                for (String userName : message.getUserNameList()) {
                    // if current user is not following userName already,
                    // and (double check) userName is not followed by current user//
                    if (currentUser().isFollowing(userName) &&
                            getUser(userName).isFollowedBy(currentUser().getName())) {
                        //add userName to current user followingList//
                        currentUser().getFollowingList().put(userName, getUser(userName));
                        // add current user to userName followersList//
                        getUser(userName).getFollowersList().put(currentUser().getName(), currentUser());
                        userNameList += userName + '\0' ;
                        ++successful;
                    }
                }
                if (successful != 0){
                    connections.send(connectionId, new FollowAckMsg( message.getOpCode(), successful, userNameList));
                }
                else{ // successful is 0//
                    connections.send(connectionId, new ErrorMsg(message.getOpCode()));
                }
            }
            else{  // message is unFollow//
                for (String userName : message.getUserNameList()) {
                    // if current user is following userName,
                    //and (double check) userName is followed by current user //
                    if (currentUser().getFollowingList().containsKey(userName) && getUser(userName).isFollowedBy(currentUser().getName())) {
                        //remove userName to current user followingList//
                        currentUser().getFollowingList().remove(getUser(userName).getName());
                        // remove current user from userName followersList//
                        getUser(userName).getFollowersList().remove(currentUser().getName());
                        ++successful;
                    }
                }
                if (successful != 0){
                    connections.send(connectionId, new FollowAckMsg(message.getOpCode(), successful, userNameList));
                }
                else{ // successful is 0//
                    connections.send(connectionId, new ErrorMsg(message.getOpCode()));
                }

            }

        }
        else {  //current user is not logged//
            connections.send(connectionId, new ErrorMsg(message.getOpCode()));
        }


    }

    private void processLogout(LogoutMsg message) {
        //if user is logged in//
        if(currentUser().isLogged()){
            //take out from loggedUsers//

               connections.send(connectionId, new AckMsg(message.getOpCode()));
               connections.disconnect(connectionId);
               dataBase.logoutUser(connectionId);
               this.shouldTerminate = true;

        }
        else {
            connections.send(connectionId, new ErrorMsg(message.getOpCode()));
        }

    }

    private void processLogin(LoginMsg message) {
        for (User user : dataBase.getUsersByName().values()){
            //compare names//
            if (message.getUserName().equals(user.getName())){
                //compare passwords//
                if(message.getPassword().equals(user.getPassword())){
                    //check if already logged//
                    if(!user.isLogged()){
                        //login the user
                        dataBase.logInUser(user, connectionId);
                        // send the user all of his waiting messages and remove them from NotReadMessageQueue//
                        for (Message msg : user.getNotReadMessageQueue()){
                            connections.send(getUserConnectionId(user.getName()), msg);
                            user.getNotReadMessageQueue().remove(msg);
                        }
                        connections.send(connectionId, new AckMsg(message.getOpCode()));
                    }

                }
            }
            else {
                connections.send(connectionId, new ErrorMsg(message.getOpCode()));
            }
        }
    }

    private void processRegister(RegisterMsg message) {
        for (String userName : dataBase.getUsersByName().keySet()){
            // if user has already registered//
            if(userName.equals(message.getUserName())){
                connections.send(connectionId, new ErrorMsg(message.getOpCode()));
                return;
            }
        }
        //if you are here then the user is not registered//
        User user = new User (message.getUserName(), message.getPassword());
        dataBase.registerUser(user);
        connections.send(connectionId, new AckMsg(message.getOpCode()));
    }


    private User currentUser(){
        return dataBase.getUsersByConnectionId().get(connectionId);
    }
    private User getUser( String userName){
        return dataBase.getUsersByName().get(userName);
    }
    private int getUserConnectionId(String userName){
        int connectionId =-1;
        for (int connection: dataBase.getUsersByConnectionId().keySet()){
            //if the name mapped to the connection equals the user name, return this connection//
            if (dataBase.getUsersByConnectionId().get(connection).getName().equals(userName)){
                connectionId =connection;
            }
        }
        return connectionId;
    }
    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }

}
