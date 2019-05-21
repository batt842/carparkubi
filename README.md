# Coding Challenge
**Carpark Ubi**

## Basic Idea
1. As many cars as possible should be charged in fast charging mode. 
2. A car occupies fast charging mode for the longest time will be switched to slow charging mode.
3. A car occupies slow charging mode for the longest time will be switched to fast charging mode.

## How to run the Application
`$ ./gradlew bootRun`

The default port is 8080.

If an API call is invoked a message like the following will be printed. 
```
2019-05-21 12:02:49.464  INFO 86473 --- [nio-8080-exec-1] c.c.c.c.ChargingPointController          : CP1 sends a notification that a car is plugged
2019-05-21 12:02:49.465  INFO 86473 --- [nio-8080-exec-1] c.c.c.c.ChargingPointController          : Report:
CP1 OCCUPIED 20A
CP2 AVAILABLE
CP3 AVAILABLE
CP4 AVAILABLE
CP5 AVAILABLE
CP6 AVAILABLE
CP7 AVAILABLE
CP8 AVAILABLE
CP9 AVAILABLE
CP10 AVAILABLE
```

## APIs
### /{id}/plug
It is called when you plug a car to a CP.

### /{id}/unplug
It is called when you unplug a car from a CP.

### API use-case
```
$ curl -XPUT "http://localhost:8080/CP1/plug"
{"result":"OK","type":"Fast"}
$ curl -XPUT "http://localhost:8080/CP2/plug"
{"result":"OK","type":"Fast"}
$ curl -XPUT "http://localhost:8080/CP1/unplug"
{"result":"OK"}
```

This is a text formatted test case.
When the final command is entered, CP1 will be switched into fast charging mode.
```
curl -XPUT "http://localhost:8080/CP1/plug"
curl -XPUT "http://localhost:8080/CP2/plug"
curl -XPUT "http://localhost:8080/CP3/plug"
curl -XPUT "http://localhost:8080/CP4/plug"
curl -XPUT "http://localhost:8080/CP5/plug"
curl -XPUT "http://localhost:8080/CP6/plug"
curl -XPUT "http://localhost:8080/CP7/plug"
curl -XPUT "http://localhost:8080/CP8/plug"
curl -XPUT "http://localhost:8080/CP9/plug"
curl -XPUT "http://localhost:8080/CP10/plug"
curl -XPUT "http://localhost:8080/CP7/unplug"
```

## How to run the Unit/Integration Tests
`$ ./gradlew test`

A test report will be generated on `./build/reports/tests/test`
