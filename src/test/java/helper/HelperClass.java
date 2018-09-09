package helper;

import io.restassured.http.ContentType;
import java.util.logging.Logger;
import static io.restassured.RestAssured.given;

public class HelperClass {

    Logger logger = Logger.getLogger(String.valueOf(getClass()));

    public void helperChangeDescriptionOfTheRoom(String access_token, String jsonString, String roomId){
        given().
                auth().oauth2(access_token).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(jsonString).
        when().
                put("https://api.gitter.im/v1/rooms/" + roomId );
    }

    public void helperJoinToTheRoom(String access_token, String roomId, String userID){
        given().
                auth().oauth2(access_token).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body("{\"id\":\"" + roomId + "\"}").
        when().
                post("https://api.gitter.im/v1/user/" + userID + "/rooms");
    }

    public void helperSendMessage(String access_token, String roomId, String message){
        given().
                auth().oauth2(access_token).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body("{\"text\":\"" + message +"\"}").
        when().
                post("https://api.gitter.im/v1/rooms/" + roomId + "/chatMessages");

        logger.info("Message was added.");
    }
}
