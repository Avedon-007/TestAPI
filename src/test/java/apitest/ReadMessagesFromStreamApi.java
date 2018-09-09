package apitest;

import org.json.JSONObject;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReadMessagesFromStreamApi {

    private String access_token;
    private String roomId;
    private List<String> jsonsArrayList = new ArrayList<>();

    public ReadMessagesFromStreamApi(String access_token, String roomId) {
        this.access_token = access_token;
        this.roomId = roomId;
    }

    public List<String> readMessage() {
        try {
            URL url = new URL("https://stream.gitter.im/v1/rooms/" + roomId + "/chatMessages?access_token=" + access_token);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;
            String parsedText;
            int i = 0;
            while (i < 3) {
                inputLine = in.readLine();
                JSONObject jsonObj = new JSONObject(inputLine);
                parsedText = (String) jsonObj.get("text");
                jsonsArrayList.add(parsedText);
                i++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonsArrayList;
    }

    public List<String> getJsonsArrayList() {
        return jsonsArrayList;
    }
}