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
### POST /v1/cps/{id}
It is called when you plug a car to a CP.

### DELETE /v1/cps/{id}
It is called when you unplug a car from a CP.

### GET /v1/cps/report
It shows the overall report of all CPs.

### API use-case
```
$ curl -XPOST "http://localhost:8080/v1/cps/CP1"
{"result":"OK","type":"Fast"}
$ curl -XPOST "http://localhost:8080/v1/cps/CP2"
{"result":"OK","type":"Fast"}
$ curl -XDELETE "http://localhost:8080/v1/cps/CP1"
{"result":"OK"}
```

This is a text formatted test case.
When the final command is entered, CP1 will be switched into fast charging mode.
```
curl -XPOST "http://localhost:8080/v1/cps/CP1"
curl -XPOST "http://localhost:8080/v1/cps/CP2"
curl -XPOST "http://localhost:8080/v1/cps/CP3"
curl -XPOST "http://localhost:8080/v1/cps/CP4"
curl -XPOST "http://localhost:8080/v1/cps/CP5"
curl -XPOST "http://localhost:8080/v1/cps/CP6"
curl -XPOST "http://localhost:8080/v1/cps/CP7"
curl -XPOST "http://localhost:8080/v1/cps/CP8"
curl -XPOST "http://localhost:8080/v1/cps/CP9"
curl -XPOST "http://localhost:8080/v1/cps/CP10"
curl -XDELETE "http://localhost:8080/v1/cps/CP7"
```

## How to run the Unit/Integration Tests
`$ ./gradlew test`

A test report will be generated on `./build/reports/tests/test`
