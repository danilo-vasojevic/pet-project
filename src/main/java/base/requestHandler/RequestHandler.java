package base.requestHandler;

import base.data.ResponseStatus;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;

@Component("requestHandler")
public class RequestHandler {

    private static final String STATUS = "status";

    @Value("${host}")
    private String host;

    @Value("${port}")
    private String port;

    @Value("${queryString}")
    private String queryString;

    private Response lastResponse;

    public void postWithJSONasBody(JSONObject jsonObj) {
        lastResponse =
                given().
                        accept(ContentType.JSON.toString()).
                        contentType(ContentType.JSON.toString()).
                        body(jsonObj.toString()).
                when().
                        post(getRequestURL()).
                then().
                        extract().response();
    }

    public int getHTTPStatusCode() {
        return lastResponse.getStatusCode();
    }

    public ResponseStatus getResponseStatus() {
        String responseBodyString = lastResponse.body().asString();
        if(isValidJSON(responseBodyString)) {
            JSONObject responseBodyJSON = new JSONObject(responseBodyString);
            return ResponseStatus.tryGetValue(responseBodyJSON.getString(STATUS));
        }
        else return ResponseStatus.UNKNOWN;
    }

    private String getRequestURL() {
        return "".equals(port)
                ? format("http://%s/%s", host, queryString)
                : format("http://%s:%s/%s", host, port, queryString);
    }

    private boolean isValidJSON(String s) {
        try {
            new JSONObject(s);
        } catch (JSONException ex) {
            try {
                new JSONArray(s);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}
