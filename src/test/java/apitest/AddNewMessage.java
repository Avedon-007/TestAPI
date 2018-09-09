package apitest;

import io.restassured.http.ContentType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static io.restassured.RestAssured.given;

public class AddNewMessage extends Thread {
    private String access_token;
    private String roomId;
    private String message;
    Logger logger = Logger.getLogger(String.valueOf(getClass()));
    private List<String> arrayistOfMessagesWithDate = new ArrayList<>();

    public AddNewMessage(String access_token, String roomId, String message){
        this.access_token = access_token;
        this.roomId = roomId;
        this.message = message;
    }

    @Override
    public void run(){
        for(int i = 0; i < 3; i++){
            String messageWithDate = message + LocalDateTime.now();
           given().
                    auth().oauth2(access_token).
                    contentType(ContentType.JSON).
                    accept(ContentType.JSON).
                    body("{\"text\":\"" + messageWithDate + "\"}").
           when().
                    post("https://api.gitter.im/v1/rooms/" + roomId + "/chatMessages");

           getMessagesWithDate(messageWithDate);
           logger.info("Message was added.");
        }
    }

    public List<String> getMessagesWithDate(String newMessage){
        arrayistOfMessagesWithDate.add(newMessage);
        return  arrayistOfMessagesWithDate;
    }

    public List<String> getArrayistOfMessagesWithDate() {
        return arrayistOfMessagesWithDate;
    }
}
