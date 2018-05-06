package base.outputGenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component("outputGenerator")
public class OutputGenerator {

    @Value("${outputFolder}")
    private String outputFolder;

    @Value("${resultFileName}")
    private String resultFileName;

    public void writeToOutputFile(String testCasePath, int statusCode, String result) {
        BufferedWriter bw = null;
        String resultString = String.join("::",
                testCasePath,
                "" + statusCode,
                result);
        try {
            String outputPath = String.format("%s/%s", outputFolder, resultFileName);
            File file = new File(outputFolder);
            if(!file.exists()) file.mkdir();
            bw = new BufferedWriter(new FileWriter(outputPath, true));
            bw.write(resultString);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException ignored) {}
        }
    }

}
