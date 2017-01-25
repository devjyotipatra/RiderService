1. Command to start the service

This will start the webservice. In case you have port 8090 occupied by some other daemon, please change the port attribute in start.ini file in the root directory of the project.

cd RiderService
java -jar start.jar


2. API call to add driver location 
curl -X POST -H "Content-Type: application/json" -d  "{\"latitude\":11.0939, \"longitude\":6.90332}" http://127.0.0.1:8090/gojek-riderservice/gojek/drivers/1/location
curl -X POST -H "Content-Type: application/json" -d  "{\"latitude\":12.0939, \"longitude\":5.90332}" http://127.0.0.1:8090/gojek-riderservice/gojek/drivers/2/location


3.API call to get drivers and their locations
curl  "http://127.0.0.1:8090/gojek-riderservice/gojek/drivers?latitude%2F12.0939&longitude%2F5.90332"


 



