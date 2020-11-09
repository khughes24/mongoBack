DEV Challenge 2 Read me

Index:

About
Postman
DB set up
Accessing the api
Extra details



About:
The program in source control is an attempt to solve the problem in the brief for the developer chalenge part 2 appychat.
Please download the program and run it to access the api, please find the details on how to access the api, postman and DB setup below.


Postman:
Please use the link below to access the postman requests:
https://www.getpostman.com/collections/bcbeb3ce94c6f80eb834



DB set up:
Please download and install mongoDB. The steps for this can be found at:
https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/
Please download and install mongodb comunity compass to view the data in the DB and set up the DB collections. The steps for this can be found at:
https://www.mongodb.com/try/download/compass



Accessing the api:
To access the api after setting up the db hit run and go to the following address:
http://localhost:8080/mavenTest_war2/webapi/
You will then be able to make requests using the browser or postman



Extra details:
Swagger has been partically set up but i have been uable to get the swagger UI working. To access a very bare bones swagger please go to:
http://localhost:8080/mavenTest_war2/webapi/swagger.json
For some reason using PATCH requests breaks the pathing and methods for the API so i have switch it to use a POST instead. This is in the porcess of being fixed
I am not able to retrieve the reactionCount for Post, Comment and Reply so i'm unable to add to their counts. I am still able to add to the reaction list though.






