set gsonpath=%cd%\lib\gson-2.8.2.jar
set shareddatapath=%cd%\lib\shared_data.jar
set mysqlconnector=%cd%\lib\mysql-connector-java-8.0.18.jar
set binpath=%cd%\bin
cd src
javac -d %binpath% -cp %gsonpath%;%shareddatapath%;%mysqlconnector%;. server/ServerMain.java
cd ..
pause