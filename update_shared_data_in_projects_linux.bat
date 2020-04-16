export root_path=$PWD
cd shared_data
cd bin
jar cf shared_data.jar shared_data
cp shared_data.jar $root_path/shared_data.jar
rm shared_data.jar
cd ..
cd ..

cd server
cd lib
export server_lib_path=$PWD
cd ..
cd ..
cp shared_data.jar $server_lib_path/shared_data.jar

cd client
cd lib
export client_lib_path=$PWD
cd ..
cd ..
cp shared_data.jar $client_lib_path/shared_data.jar

cd server_directory
cd lib
export server_directory_lib_path=$PWD
cd ..
cd ..
cp shared_data.jar $server_directory_lib_path/shared_data.jar

rm $root_path/shared_data.jar