package base.requestHandler;

import base.data.ResponseStatus;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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
        JSONObject responseBodyJSON = new JSONObject(lastResponse.body().print());
        return ResponseStatus.tryGetValue(responseBodyJSON.getString(STATUS));
    }

    private String getRequestURL() {
        return "".equals(port)
                ? format("http://%s/%s", host, queryString)
                : format("http://%s:%s/%s", host, port, queryString);
    }

}
