# High level workflow

Application starts from `main` method within `base.Application` class. It instantiates xml application context object, fetches testSuite bean (can actually fetch any class which implements `RunnableTestSuite` interface (this nicely illustrates Springs magical wiring mechanisms :-) )) and call `void run();` method it overrides.

There we fetch all *.json* files found at directory specified by *inputFolder* mandatory parameter and for each of them does the following:
  1. Replace `server-key` key with `CONST_SERVER_KEY` property
  2. Add modified *.json* file to request
  3. Make post request to SUT (URL is created from other properties)
  4. Fetch response. Parse it's body as `JSONObject` extract it's `status` key and fetch response status code.
  5. Append result of test execution to output file (output file path and file name are specified in properties file)
  

# Overview of the project
This small project is me playing with maven dependencies and plugins, as well as Spring Framework to create a small API testing tool.

To run the project, navigate to project root dir and from there run:
`mvn clean install exec:java -DinputFolder=src/main/resources/testCases`
This will take all *.json* files from that specified directory and execute intended test using them. Note that setting *inputFolder* parameter is mandatory, while other parameters are not. Full command would look like this:
```
mvn clean install exec:java 
  -DinputFolder=src/main/resources/testCases
  -DoutputFolder=target/executionResult
  -DresultFileName=results.txt
  -Dhost=demo4657191.mockable.io
  -Dport=8080
  -DqueryString=test
  -DCONST_SERVER_KEY=DefaultBADANBgkqhkiG9w0
```
## applicationContext.xml
Standard Spring context xml file, used to specify *.properties* file (containing default property values) and specifying base package to wire up the project using `@Autowire` annotation.
```xml
<context:annotation-config/>
<context:property-placeholder location="test.properties"/>

<context:annotation-config/>
<context:component-scan base-package="base"/>
```
## pom.xml File
In POM file I use only a handfull of dependencies and plugins and they serve a single, small purpose needed to achieve project result
### Dependencies
Extending basic Java functionalities with external libraries to make code a bit prettier and more readable, while also making it easier to accomplish project's goals.
#### *RestAssured*
I use RestAssured in RequestHandler class to seamlessly create requests to SUT, fetch response and parse response body strgin.
#### *Spring Framework*
I use Spring to wire up the entire project. This is best shown throuth usage of properties, and autowiring of components needed by MyTestSuite.java class to execute tests.
#### *JSON In Java*
Just a quick way of tossing JSON files to string and vice-versa. Also used to fetch keys from JSON objects and checking if string is a valid JSON file.
### Plugins
#### *Exec Maven Plugin (MojoHouse)*
I use this plugin to make it easier to run the project. Just call `mvn exec:java` (while specifying mandatory parameters) from command line while in working directory and that's it. Main thing here is to specify main class to run the project
```xml
<mainClass>base.Application</mainClass>
```
#### *Maven Enforcer Plugin - The Loving Iron Fist of Maven*
I use this plugin to ensure mandatory paramter is passed via command line and to print out message if it isn't.
```xml
<configuration>
    <rules>
        <requireProperty>
            <property>inputFolder</property>
            <message>[ERROR] You must set a inputFolder property!</message>
        </requireProperty>
    </rules>
    <fail>true</fail>
</configuration>

```
