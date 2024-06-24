# Java API

I had to write simple HTML and JavaScript that use [fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch) to perform [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations on a json API for a college assignment. Due to the lack of a free API with unlimited access to all necessary operations, I developed this API using [Springboot](https://spring.io/projects/spring-boot).
It accesses the same [PostgreSQL](https://www.postgresql.org/) database as my previous Springboot project, [JavaBackend](https://github.com/edu9988/JavaBackend/). Originally hosted on [render.com](https://render.com/) for a free month ([https://javaapi-d1ez.onrender.com/](https://javaapi-d1ez.onrender.com/)).

## Operations supported

| Action | HTTP | Payload | URL | Description
| :--- | :--- | :--- | :--- | :---
| Create | POST | json | <nobr>/api/users</nobr> | Insert user into database
| Read | GET | - | <nobr>/api/users</nobr> | Get all users from database
| Read | GET | - | <nobr>/api/users/\<id\></nobr> | Get a single user
| Update | PUT | json | <nobr>/api/users/\<id\></nobr> | Update user with payload
| Delete | DELETE | - | <nobr>/api/users/\<id\></nobr> | Remove user from database
| Authenticate | POST | json | <nobr>/api/users/auth</nobr> | Checks if user and password in payload is in the database
