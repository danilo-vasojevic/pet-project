package base.testCaseProvider;

import base.testData.PetProjectTestData;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import static java.nio.file.Files.readAllLines;
import static java.nio.file.Files.walk;

@Component("testCaseProvider")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MyTestCaseProvider implements TestCaseProvider {

    private static final String SERVER_KEY = "server_key";
    private static final String TEST_DATA = "test_data";

    @Value("${inputFolder}")
    private String inputFolder;

    @Value("${CONST_SERVER_KEY}")
    private String constServerKey;

    private ArrayList<PetProjectTestData> testCases = new ArrayList<>();

    public ArrayList<PetProjectTestData> getTestCases() {
        return testCases;
    }

    @Override
    public TestCaseProvider prepareTestCases() {
        try (Stream<Path> paths = walk(Paths.get(inputFolder))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(this::getTestData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    private void getTestData(Path path){
        String pathAsString = path.normalize().toString();
        try {
            String json = String.join("", readAllLines(Paths.get(pathAsString)));
            JSONObject jsonObject = new JSONObject(json);
            jsonObject.put(SERVER_KEY, constServerKey);
            testCases.add(new PetProjectTestData(jsonObject.getString(SERVER_KEY), jsonObject.getString(TEST_DATA), jsonObject, path));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
