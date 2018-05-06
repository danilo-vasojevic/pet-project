package base.testCaseProvider;

import base.testData.PetProjectTestData;

import java.util.ArrayList;

public interface TestCaseProvider {
    TestCaseProvider prepareTestCases();
    ArrayList<PetProjectTestData> getTestCases();
}
