set gsonpath=%cd%\lib\gson-2.8.2.jar
set shareddatapath=%cd%\lib\shared_data.jar
set binpath=%cd%\bin
cd src
javac -d %binpath% -cp %gsonpath%;%shareddatapath%;. server_directory/ServerDirectoryMain.java
cd ..
pause