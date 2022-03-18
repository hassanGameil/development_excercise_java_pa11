@echo off
START "" /B java -jar Server.jar first_server.properties
START "" /B java -jar Server.jar second_server.properties
java -jar Client.jar client.properties
pause