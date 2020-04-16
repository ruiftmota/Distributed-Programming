
set gsonpath=%cd%\lib\gson-2.8.2.jar
set shareddatapath=%cd%\lib\shared_data.jar
set music_path=%cd%\music_path
cd bin
java -cp %music_path%;%gsonpath%;%shareddatapath%;. client.ClientMain 192.168.1.75 52047

pause