package base.testData;

import org.json.JSONObject;

import java.nio.file.Path;

public class PetProjectTestData {

    public String serverKey;
    public String testData;
    public transient JSONObject jsonObject;
    public transient Path filePath;

    public PetProjectTestData(String serverKey, String testData, JSONObject jsonString, Path filePath) {
        this.serverKey = serverKey;
        this.testData = testData;
        this.jsonObject = jsonString;
        this.filePath = filePath;
    }
}
