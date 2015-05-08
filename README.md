# Camunda REST deployment example

This little example demonstrates how to create a deployment
for Camunda BPM using the REST API and the Apache HTTP Client.

## Test It

Adjust the hostname in `src/main/java/org/camunda/bpm/RestDeployment.java` if it isn't `localhost`. Then run:

```
mvn compile exec:java -Dexec.mainClass=org.camunda.bpm.RestDeployment -Dexec.arguments=process1.bpmn,process2.bpmn,process3.bpmn
```
