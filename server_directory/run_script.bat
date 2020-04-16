set gsonpath=%cd%\lib\gson-2.8.2.jar
set shareddatapath=%cd%\lib\shared_data.jar
cd bin
java -cp %gsonpath%;%shareddatapath%;. server_directory.ServerDirectoryMain
cd ..
pause