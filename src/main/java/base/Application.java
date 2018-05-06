package base;

import base.suite.RunnableTestSuite;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

    public static void main(String[] args) throws Throwable {
        new ClassPathXmlApplicationContext("applicationContext.xml")
                .getBean("testCaseRepo", RunnableTestSuite.class)
                .run();
    }
}
