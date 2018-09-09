package apitest;

import helper.AddNewMessage;
import helper.HelperClass;
import helper.ReadMessagesFromStreamApi;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import java.util.logging.Logger;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.assertEquals;


public class TestGitterApi {

    private String jsonOld =  "{\"topic\":\"\"}"; // {"topic":""}
    private String jsonNewDescription =  "{\"topic\":\"NEW NEW NEW description of First Room\"}";
    private String access_token = "f54ab624128d648a74e7d21e4d9c5c45680ae86b";
    private String roomId = "5b92e57bd73408ce4fa74b12";
    private String userID = "5b92e210d73408ce4fa74ac3";
    private volatile String  text = "This is my new message in the First room. ";
    private HelperClass helperClass = new HelperClass();

    /**
     * This test checks quantity of rooms in community and name of each room.
     */
    @Test(priority = 2)
    public void testCheckRooms(){
        given().
                auth().oauth2(access_token).
        when().
                get("https://api.gitter.im/v1/rooms").
        then().
                assertThat().body("size()",is(2)).
                assertThat().body("name[0]", equalTo("NothingSpecial/Lobby")).
                assertThat().body("name[1]", equalTo("NothingSpecial/FirstRoom"));
    }

    /**
     * This test checks possibility of changing of the room description
     */
    @Test(priority = 3)
    public void testChangeDescriptionOfRoom(){
        given().
                auth().oauth2(access_token).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(jsonNewDescription).
        when().
                put("https://api.gitter.im/v1/rooms/" + roomId ).
        then().
                body("topic", equalTo("NEW NEW NEW description of First Room"));

        // Clear the room description
        helperClass.helperChangeDescriptionOfTheRoom(access_token, jsonOld, roomId);
    }

    /**
     * This test checks possibility to leave room.
     */
    @Test(priority = 4)
    public void testLeaveTheRoom(){
        given().
                auth().oauth2(access_token).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                delete("https://api.gitter.im/v1/rooms/" + roomId + "/users/" + "5b92e210d73408ce4fa74ac3").
        then().
                assertThat().body("size()", is(1));
    }

    /**
     * This test checks possibility to join the room.
     */
    @Test(priority = 5)
    public void testJoinToTheRoom(){
        helperClass.helperJoinToTheRoom(access_token, roomId, userID);

        // Assert that the room was added.
        testCheckRooms();
    }

    /**
     * This test checks possibility to listening new messages through the Streaming API.
     */
    @Test(priority = 1)
    public void testSendMessages(){
        // Sending new messages to specific room
        AddNewMessage addNewMessage = new AddNewMessage(access_token , roomId, text);
        addNewMessage.start();

        // Reading new messages from Streaming API
        ReadMessagesFromStreamApi readMessagesFromStreamApi = new ReadMessagesFromStreamApi(access_token , roomId);
        readMessagesFromStreamApi.readMessage();
           assertEquals(addNewMessage.getArrayistOfMessagesWithDate(), readMessagesFromStreamApi.getJsonsArrayList(),
                   "ERROR!!!");
        }
}
