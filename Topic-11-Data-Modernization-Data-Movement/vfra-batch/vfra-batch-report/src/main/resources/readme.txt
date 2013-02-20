To try out report app in STS:
-----------------------------
1. Update vfra-batch-report.properties to use H2 settings
2. Run the QueryDslTest test in STS (which will create H2 db and some initial data that will persist after test because DB_CLOSE_ON_EXIT=FALSE is used in H2 connection URL)
3. Deploy vfra-batch-report to STS tc Server instance
4. In browser, open http://localhost:8080/vfra-batch-report/order/report


To test out rest shell against REST apis:
-----------------------------------------
1. Run the QueryDslTest test in STS (which will create H2 db and some initial data that will persist after test because DB_CLOSE_ON_EXIT=FALSE is used in H2 connection URL)
2. Setup Java 7 JDK, add to PATH, set JAVA_HOME
3. download and mvn install: https://github.com/SpringSource/rest-shell 
4. Run rest-shell/build/install/rest-shell/bin/rest-shell
5. Test order/history (see RestOrderController)
http://localhost:8080:> follow vfra-batch-report/order/history
http://localhost:8080/vfra-batch-report/order/history:> get --params "{type: "BUY", timeStamps: 3}"
> GET http://localhost:8080/vfra-batch-report/order/history/?type=BUY&timeStamps=3

< 200 OK
< Server: Apache-Coyote/1.1
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Thu, 20 Dec 2012 21:07:35 GMT
< 
[ [ "10/29/12 10:45 AM", 2 ], [ "10/30/12 10:45 AM", 2 ], [ "10/31/12 10:45 AM", 2 ] ]
http://localhost:8080/vfra-batch-report/order/history:> 

6. Test REST Exporter (see web.xml)
http://localhost:8080/vfra-batch-report:> follow orderx
http://localhost:8080/vfra-batch-report/orderx:> list order
rel             href                                                                 
=====================================================================================
order.Order     http://localhost:8080/vfra-batch-report/orderx/order/1:1351532709000 
order.Order     http://localhost:8080/vfra-batch-report/orderx/order/2:1351532709000 
order.Order     http://localhost:8080/vfra-batch-report/orderx/order/3:1351532709000 
order.Order     http://localhost:8080/vfra-batch-report/orderx/order/4:1351532709000 
order.Order     http://localhost:8080/vfra-batch-report/orderx/order/5:1351619109000 
order.Order     http://localhost:8080/vfra-batch-report/orderx/order/6:1351619109000 
order.Order     http://localhost:8080/vfra-batch-report/orderx/order/7:1351619109000 
order.Order     http://localhost:8080/vfra-batch-report/orderx/order/8:1351705509000 
order.Order     http://localhost:8080/vfra-batch-report/orderx/order/9:1351705509000 
order.Order     http://localhost:8080/vfra-batch-report/orderx/order/10:1351705509000
order.search    http://localhost:8080/vfra-batch-report/orderx/order/search          

http://localhost:8080/vfra-batch-report/orderx:> get order/6:1351619109000
> GET http://localhost:8080/vfra-batch-report/orderx/order/6:1351619109000

< 200 OK
< Server: Apache-Coyote/1.1
< Content-Type: application/json
< Content-Length: 375
< Date: Thu, 20 Dec 2012 21:11:20 GMT
< 
{
  "links" : [ {
    "rel" : "self",
    "href" : "http://localhost:8080/vfra-batch-report/orderx/order/6:1351619109000"
  } ],
  "price" : 118.65,
  "accountId" : 32769,
  "orderType" : 1,
  "quoteSymbol" : "amzn",
  "holdingId" : 9,
  "quantity" : 132.75,
  "openDate" : 1346310000000,
  "orderStatus" : "closed",
  "completionDate" : 1346310000000,
  "orderFee" : 10.5
}
http://localhost:8080/vfra-batch-report/orderx:> 
