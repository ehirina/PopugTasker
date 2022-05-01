Training project for async architecture course.
Jira-ish for popug workers.
# Popug Tasker
> Training project for async architecture course.
  Jira-ish for popug workers.

System uses rabbitmq for message exchange and mongoDB for accounts info storage

## Task
> Service is containing the logic of task manager
- API documentation can be access at http://localhost:8080/swagger-ui/index.html
### Run locally
- `mvn clean package` to install needed dependency
- ` java -jar target/tasker-0.0.1-SNAPSHOT.jar` to run 


## access_token_server
> Service is containing all authentification & authorization logic
- API documentation can be access at https://localhost:2600/api-docs/

### Run locally
- `yarn install` to install needed dependency
- `yarn start` to run 


*Implemented* 
- On creation of account stream data in task service 
- Creation of a popug entity in task service after reading cud-event

*In progress*
- verification of jwt token when requesting data in task manager
- shuffle functionality in task service
 