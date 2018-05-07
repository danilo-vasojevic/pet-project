package base.suite;

import base.outputGenerator.OutputGenerator;
import base.requestHandler.RequestHandler;
import base.testCaseProvider.TestCaseProvider;
import base.testData.PetProjectTestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("testCaseRepo")
public class MyTestSuite implements RunnableTestSuite {

    @Autowired
    private TestCaseProvider testCaseProvider;

    @Autowired
    private OutputGenerator outputGenerator;

    @Autowired
    private RequestHandler requestHandler;

    private int statusCode;
    private String responseStatus;

    @Override
    public void run() {
           testCaseProvider
                   .prepareTestCases()
                   .getTestCases()
                   .forEach(this::executeTestCase);
    }

    private void executeTestCase(PetProjectTestData petProjectTestData) {
        String testCaseFilePath = petProjectTestData.filePath.toAbsolutePath().toString();
        try {
            requestHandler.postWithJSONasBody(petProjectTestData.jsonObject);
            statusCode = requestHandler.getHTTPStatusCode();
            responseStatus = requestHandler.getResponseStatus().toString();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            outputGenerator.writeToOutputFile(testCaseFilePath, statusCode, responseStatus);
        }
    }
}
