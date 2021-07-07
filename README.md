
# Solution :

1. Kindly find the docker-compose file in the root dir to start the env and app
        using `mvn clean package && docker-compose up -d`
        
   ##### then you can connect to this url to see the api swagger docs `{dockerhost}:8080/api-docs` and you can test them
 
 
  // Kindly note that if you want to run the integration tests & start the app locally,
        you will need to update
         the following property in docker-compose file for the kafka broker to your docker host : 
         KAFKA_ADVERTISED_HOST_NAME
         in my case it will be : KAFKA_ADVERTISED_HOST_NAME: 192.168.99.100 
         as i use the old docker on vm for windows

2. Run integration test for testing setup `mvn clean test`
   
    // Then you can remove the containers and start them again to run the application on the local machine,
        for sure you can remove the creation of the application container itself as you will start it locally not in container        
    // Not fully tested, but the creation of an employee is tested through kafka consumer
    
        
3. Start the application by using `mvn clean spring-boot:run`

4. build the docker image for the application run `sh ./scripts/build-image.sh`




----------------------------------------------------------------------------------------------------

PeopleFlow (www.pplflw.com) is a global HR platform enabling companies to hire & onboard their employees internationally, at the push of a button. It is our mission to create opportunities for anyone to work from anywhere. As work is becoming even more global and remote, there has never been a bigger chance to build a truly global HR-tech company.


As a part of our backend engineering team, you will be responsible for building our core platform including an  employees managment system.

The employees on this system are assigned to different states, Initially when an employee is added it will be assigned "ADDED" state automatically .


The other states (State machine) for A given Employee are:
- ADDED
- IN-CHECK
- APPROVED
- ACTIVE


Our backend stack is:
- Java 11 
- Spring Framework 
- Kafka

Your task is to build  Restful API doing the following:
- An Endpoint to support adding an employee with very basic employee details including (name, contract information, age, you can decide.) With initial state "ADDED" which incidates that the employee isn't active yet.

- Another endpoint to change the state of a given employee to "In-CHECK" or any of the states defined above in the state machine 


Please provide a solution with the  above features with the following consideration.

- Being simply executable with the least effort Ideally using Docker and docker-compose or any smailiar approach.
- For state machine could be as simple as of using ENUM or by using https://projects.spring.io/spring-statemachine/ 
- Please provide testing for your solution.
- Providing an API Contract e.g. OPENAPI spec. is a big plus




