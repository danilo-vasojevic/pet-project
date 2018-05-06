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
    public TestCaseProvider testCaseProvider;

    @Autowired
    public OutputGenerator outputGenerator;

    @Autowired
    public RequestHandler requestHandler;

    @Override
    public void run() {
        testCaseProvider
                .prepareTestCases()
                .getTestCases()
                .forEach(this::executeTestCase);
    }

    private void executeTestCase(PetProjectTestData petProjectTestData) {
        requestHandler.postWithJSONasBody(petProjectTestData.jsonObject);

        int httpStatusCode = requestHandler.getHTTPStatusCode();
        String responseStatus = requestHandler.getResponseStatus().getStatusString();
        String testCaseFilePath = petProjectTestData.filePath.toAbsolutePath().toString();

        outputGenerator.writeToOutputFile(testCaseFilePath, httpStatusCode, responseStatus);

    }
}
