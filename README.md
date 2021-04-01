# Spendit 
# RestFul Api /Backend
<br>

## Description

This is a RestFul api for Authentication with Spring Security using JWT

## User Stories

-  **Register:** Anyone with email and password create an account
-  **Login:** User can login to the api and get the Json Web Token valid for 1 hour 
-  **Change Password:** User with JWT  can change Password by sending old and new password if user has logged in.
-  **Forgot Password:** User with JWT  can get an email with Reset Password JW Token in a link
-  **Reset Password:** User with JWT  can use the this link to reset Password by sending new Password.
-  **Logout:** User with JWT  can logout from the Api
-  **Private Area:** User with JWT can get private information

## Backlog

- Verifications for Email and strong password

<br>



## API Endpoints (backend routes)

| HTTP Method | URL    |Header| Request Body  | ResponseBody   | Description  |
| ------| -----------------| -----------| -----------------| --------------- |------------------|
| POST  |`/public/register`      | Nothing | {email, password}      |{Message} |Create an User |
| POST  |`/public/authenticate`  | Nothing| {email, password}         | {JWT}  |get the JWT valid for 1 hour to use in header in next requests|
| GET   |`/public/forgot_password`| Nothing |{email}  |{Message}    | to get an email with Reset Password JWT in a link|
| POST  |`/public/reset_password`| Nothing |{newPassword,uuid}  |{Message}    |reset password by using uuid(get from link)|
| POST  |`/auth/change_password`| Valid JWT Token |{email,oldPassword,newPassword}  |{Message}|to change the password when user is already authenticated|
| GET   | `/auth/private`        |      Valid JWT Token |   Nothing    | {[data]}| to get the private information  in real case  |
| POST  | `/auth/logout`         |Valid JWT Token  |Nothing   | {Message}  |  to destroy the JWT token   |


<br>
        
## Simple Tests  with command line:

- Try to use Auth routes without user-authenticaton:
    resulted with receiving  an Unauthorized message:


```
$ curl  http://localhost:8080/auth/private

{"timestamp":"2021-04-01T09:00:36.569+00:00","status":401,"error":"Unauthorized","message":"","path":"/auth/private"}%
```


- Create a User:

```
$ curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"email":"test@gmail.com","password":"Test123."}' \
  http://localhost:8080/public/register

  User created%
```
- Try to login with user information:
- We receive the JWT token valid for 1 hour

```
$ curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"email":"test@gmail.com","password":"Test123."}' \
  http://localhost:8080/public/login

{"jwttoken":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImV4cCI6MTYxNzI3MzI4MiwiaWF0IjoxNjE3MjY5NjgyfQ.wSl1_akdGJzbFgNNNYo4K6A0OWpJzI2aXGaxr5rvi3R5XbHG7w_fZMT6vgVmkrziV-fzgjiZyylP7Su-kb4_pg"}
```
- Now try to go private area by using jwt token in Header:

```
$ curl --header "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImV4cCI6MTYxNzI3MzI4MiwiaWF0IjoxNjE3MjY5NjgyfQ.wSl1_akdGJzbFgNNNYo4K6A0OWpJzI2aXGaxr5rvi3R5XbHG7w_fZMT6vgVmkrziV-fzgjiZyylP7Su-kb4_pg" \
  --request GET \
  http://localhost:8080/auth/private

  You are in the private area now%
```

- Change Password when you logged in:

```
$ curl -H "Content-Type: application/json"  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImV4cCI6MTYxNzI3MzI4MiwiaWF0IjoxNjE3MjY5NjgyfQ.wSl1_akdGJzbFgNNNYo4K6A0OWpJzI2aXGaxr5rvi3R5XbHG7w_fZMT6vgVmkrziV-fzgjiZyylP7Su-kb4_pg" \
 --data '{"oldPassword":"Test123.","newPassword":"Abcd123."}' \ --request POST \
  http://localhost:8080/auth/change_password

  Password Successfuly Changed%

  ```

- Log Out by using JWTToken in Header

```
$ curl --header "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImV4cCI6MTYxNzI3MzI4MiwiaWF0IjoxNjE3MjY5NjgyfQ.wSl1_akdGJzbFgNNNYo4K6A0OWpJzI2aXGaxr5rvi3R5XbHG7w_fZMT6vgVmkrziV-fzgjiZyylP7Su-kb4_pg" \
  --request POST \
  http://localhost:8080/auth/logout

  You are successfully logged Out%
```

- Forgot Password:

```
$ curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"email":"test@gmail.com"}' \
  http://localhost:8080/public/forgot_password

  Reset password Link is sent your email%
```

- In Console:

```
: Email to:test@gmail.com
: This link should be consumed for reset password
: http://localhost:8080/public/reset_password/f1ad76fa-d360-451b-ab12-aff98db028c4

```
- Use UUID Reset Password Key to reset password:

```

 curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"newPassword":"Xyz123.", "uuid":"f1ad76fa-d360-451b-ab12-aff98db028c4"}' \
  http://localhost:8080/public/reset_password

  Your password is reset successfully%

```

<br>
        

### Git
https://github.com/erdemce/SpenditRestApi











