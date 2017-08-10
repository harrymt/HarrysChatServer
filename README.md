
# Pop3 Chat Server
[![Build Status](https://travis-ci.org/harrymt/HarrysChatServer.svg?branch=master)](https://travis-ci.org/harrymt/HarrysChatServer)
[![Coverage Status](https://coveralls.io/repos/github/harrymt/HarrysChatServer/badge.svg?branch=master)](https://coveralls.io/github/harrymt/HarrysChatServer?branch=master)
[![codebeat badge](https://codebeat.co/badges/7f5e6106-336a-4c53-9421-28e6c59292dd)](https://codebeat.co/projects/github-com-harrymt-harryschatserver-master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8d038013862047fc8340b72aa6f24d9e)](https://www.codacy.com/app/harrymt/HarrysChatServer?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=harrymt/HarrysChatServer&amp;utm_campaign=Badge_Grade)
[![BCH compliance](https://bettercodehub.com/edge/badge/harrymt/HarrysChatServer?branch=master)](https://bettercodehub.com/)

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


