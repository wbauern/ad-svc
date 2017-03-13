# ad-svc
To build:
`mvn clean install`

To run:
`java -jar target/ad-svc.jar --spring.datasource.url=<jdbc_url> --spring.datasource.username=<username> --spring.datasource.password=<password>`

Notes:
Tomcat log files will be created in /tmp/logs.  If this needs to change the set the following properties:
`--logging.path=<path>`
`--server.tomcat.accessLog.directory=<path>`
