webapp
======

For web applications. Currently there is only a Spring MVC-based web application: Simplest CI - A very simple so-called Continous Integration, developed solely for self-education purpose. Link to the application deployed on Amazon Web Service: [http://54.201.201.43:8080/autospring/home](http://54.201.201.43:8080/autospring/home).

## Technologies
1. MVC Framework: Spring 3.1
2. DBMS: Sqlite3
3. DB Access: JPA 1.0.1 interface implemented by Hibernate 4.1.4
4. Concurrency handling: Java ReadWriteLock, Java ThreadPoolExecutor, etc.
5. Build tool: Maven2

## Featured use case
My current research project (C++ Makefile project) at [https://github.com/wws2003/Research](https://github.com/wws2003/Research) is using this web application to automate the process of pull->build->run unit test. Any push to GitHub will automatically trigger a task doing above jobs on the web application. The build results are logged and can be seen at [http://54.201.201.43:8080/autospring/buildlist/4/](http://54.201.201.43:8080/autospring/buildlist/4/)

## TODO List
1. Real-time export workspace build output to browser, instead of log-file-only tracing 
2. Deployment of better, higher level concurrency handling techniques such as Non-blocking algorithms, Write-Ahead log (?) 
3. Integration of Spring Security 
4. Notification on build-failure 

