# Java API

I had to write simple HTML and JavaScript that use [fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch) to perform [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations on a json API for a college assignment. Due to the lack of a free API with unlimited access to all necessary operations, I developed this API using [Springboot](https://spring.io/projects/spring-boot).
It accesses the same [PostgreSQL](https://www.postgresql.org/) database as my previous Springboot project, [JavaBackend](https://github.com/edu9988/JavaBackend/). Originally hosted on [render.com](https://render.com/) for a free month ([https://javaapi-d1ez.onrender.com/](https://javaapi-d1ez.onrender.com/)).

## Operations supported

| Action | HTTP | Payload | URL | Description
| :--- | :--- | :--- | :--- | :---
| Create | POST | json | /api/users | Insert user into database
| Read | GET | - | /api/users | Get all users from database
| Read | GET | - | /api/users/\<id\> | Get a single user
| Update | PUT | json | /api/users/\<id\> | Update user with payload
| Delete | DELETE | - | /api/users/\<id\> | Remove user from database
| Authenticate | POST | json | /api/users/auth | Checks if user and password match some user in the database

Where json payload is of the form

```javascript
{
  "uname"  :  String,
  "pwd"    :  String
}
```

Both `uname` and `pwd` must be not null, non-empty and contain no white spaces.

### Hashed passwords

There is also a hashed version of the API available at `/api/hash`. This version supports all the same operations and functionalities as the standard API described above, with one key difference: the database hashes the provided `pwd` before storing it or comparing it with stored passwords.

This version is designed to explicitly demonstrate how hashed passwords work, which can be particularly useful for educational purposes. It showcases the importance of hashing passwords to enhance security in web applications.
