echo off
set gsonpath=%cd%\lib\gson-2.8.2.jar
set shareddatapath=%cd%\lib\shared_data.jar
set mysqlconnector=%cd%\lib\mysql-connector-java-8.0.18.jar
cd bin
java -cp %gsonpath%;%shareddatapath%;%mysqlconnector%;. server.ServerMain 59499 192.168.1.150 0 0
cd ..