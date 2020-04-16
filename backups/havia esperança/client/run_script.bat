echo off
set gsonpath=%cd%\lib\gson-2.8.2.jar
set shareddatapath=%cd%\lib\shared_data.jar
cd bin
java -cp %gsonpath%;%shareddatapath%;. client.ClientMain 192.168.1.150 62861
cd ..

pause