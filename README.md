# Pop3 Chat Server

[Written](G53SQM-psyhm1-Report.pdf) for the University of Nottingham unit, Software Quality Management.

### Build

```java
java -jar chatServer.jar
```

### Test

```bash
telnet localhost 9000
```

The project uses:

- Unit Tests using [JUnit](http://junit.org/junit5/)
- Integration Tests
- GUI tests using [FEST](https://code.google.com/archive/p/fest/)
- Pre-Flight Server with [Travis CI](https://travis-ci.org/).
- Static Analysis using [Eclipse Metrics](http://eclipse-metrics.sourceforge.net/)




#### Unit tests using [JUnit](http://junit.org/junit5/)

[ITestMESG.java](src/test/java/harry/ITestMESG.java) - example unit test

```java
@Test
public void testMesgParameterEmptyString() {
  client.sendMessage("MESG ");
  
  assertEquals(
         "Test mesg parameters as empty string",
         "BAD You have not logged in yet",
         client.getLastServerResponse()
  );
}
```

#### GUI tests using [FEST](https://code.google.com/archive/p/fest/)

```java
@Test
public void showInvalidCommandFromClient() throws InterruptedException {
  clientGUI.textBox(textFieldUserInput).enterText("MESG  ");
  clientGUI.button(btnSend).click();

  // Wait until GUI has written to text box
  clientGUI.robot.waitForIdle();

  String response = getLastResponseWritten(clientGUI.textBox(textAreaOutputText).text());
  assertEquals("Check bad server response.", response, ">Invalid Command");
}
```

#### Pre-Flight Server with [Travis CI](https://travis-ci.org/).

[.travis.yml](.travis.yml):
```yaml
language: java
jdk:
  - oraclejdk8
env:
  - DISPLAY=:99.0
before_install:
  - sh -e /etc/init.d/xvfb start
```


