# servicemanual-java

The following application is for making records of factory device maintenance tasks.

The application consists of a Java-backend and a h2-database.

Instruction on running the application:

1. Start application by running ServiceManualApplication as a Java Application. The Application will start running on port 8084 (change possible from the application.properties file)
2. The database is created based on the application.properties -file and the database can be viewed in the url: http://localhost:8084/
3. The application consists of the following API's:
   1. localhost:8084/tasks
      This retrieves all of the stored tasks in the maintenance task -database. The query can be executed using PostMan as following:
      
      <img width="661" alt="image" src="https://github.com/laurapihamaa/service_manual_excercise/assets/41535336/baf3d1a8-21a6-4262-8636-19f190272961">

   3. localhost:8084/tasks/getTask/{id}
      This retrieves one task based on the path variable provided. The query can be executed using Postman as following:
      
      <img width="634" alt="image" src="https://github.com/laurapihamaa/service_manual_excercise/assets/41535336/b0f684d7-a74d-4fe1-b631-f268ffa15136">

   5. localhost:8084/tasks/postTask
      This posts task with the given request body. The request body should include registration time, description, severity, status and factorydevice. The query can be executed using PostMan as following:
      
      <img width="352" alt="image" src="https://github.com/laurapihamaa/service_manual_excercise/assets/41535336/9c4794ec-2771-4c18-af55-67df29c1fb4d">
      
   7. localhost:8084/tasks/updateTasks/{id}
      This updates a task with the id in the path variable. The query can be executed using Postman as following:
      
      <img width="340" alt="image" src="https://github.com/laurapihamaa/service_manual_excercise/assets/41535336/d4e89672-328e-4b5b-9bf9-32febef412c1">

   9. localhost:8084/tasks/deleteTasks/{id}
      This deletes a task with the id in the path variable. The query can be executed using Postman as following:
      
      <img width="359" alt="image" src="https://github.com/laurapihamaa/service_manual_excercise/assets/41535336/4f853f0f-675e-43c7-9628-bac67dc8226e">

   11. localhost:8084/tasks/filter?deviceId={id}
      This filters the tasks based on the gicen device id. The query can be executed using Postman as following:

      <img width="356" alt="image" src="https://github.com/laurapihamaa/service_manual_excercise/assets/41535336/8c6416e8-1554-436b-93a8-1a286bb0b281">


      
Some notes on the application:
1. I tried implementing an enum validator for status and severity but for some reason couldn't get it working. Needs still some work.
2. Test coverage is aroung 80% for application but some tests could still need some more work (like creating the tasks). 
