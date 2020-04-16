set root_path=%cd%
cd shared_data
cd bin
jar cf shared_data.jar shared_data
cp shared_data.jar %root_path%/shared_data.jar
rm shared_data.jar
cd ..
cd ..

cd server
cd lib
set server_lib_path=%cd%
cd ..
cd ..
cp shared_data.jar %server_lib_path%/shared_data.jar

cd server_directory
cd lib
set server_directory_path=%cd%
cd ..
cd ..
cp shared_data.jar %server_directory_path%/shared_data.jar

cd client
cd lib
set client_lib_path=%cd%
cd ..
cd ..
cp shared_data.jar %client_lib_path%/shared_data.jar

rm %root_path%/shared_data.jar