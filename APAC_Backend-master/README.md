# APAC_Backend

All runnable jars are located in the jars folder:
1. Server.jar
This the main jar for the server.
To run the backend server, run the JAR by using the following command:
“java -jar <JAR_NAME> <DB_Configuration_File> <Communication_Port_With_Andoid_App>”

An example of the command is:
“java -jar Server.jar DB_Config.txt 1234”

The <DB_Configuration_File> contains the information of the MySQL Database credential. The file must contain the following information in order:
Database_address=<DB_IP_Address>
Database_port=<DB_Port>
Database_name=<DB_Name>
Database_username=<DB_Username>
Database_password=<DB_Password>

An example of the DB_Config.txt file is:
Database_address=//52.27.175.76
Database_port=2222
Database_name=5115_15_fall_09
Database_username=admin
Database_password=admin

The following command will show the help message to run the jar file:
“java -jar <JAR_NAME> -h”

2. AAI.jar
This is the user interface for bakcend operations such as adding FAQ.
We provide AAI in a JAR file and can be executed by:
“java -jar AAI.jar <option> <argument(s)>”

Some of the options are:
Add a new FAQ
command: “java -jar AAI.jar addFAQ <FAQ_Title> <FAQ_Description>”
<FAQ_Title>: the title of the new FAQ
<FAQ_Description>: the description of the new FAQ
Update the description of a FAQ
command: “java -jar AAI.jar updateFAQ <FAQ_Title> <New_FAQ_Description>”
<FAQ_Title>: the title of the FAQ that will be updated
<New_FAQ_Desctiption>: The new description of the FAQ
Remove a forum
command: “java -jar AAI.jar rmForum <Forum_Title>”
<Forum_Title>: The title of the forum to be removed.
Remove a FAQ
command:“ java -jar AAI.jar rmFAQ <FAQ_Title>”
<FAQ_Title>: The title of the FAQ to be removed.
 
