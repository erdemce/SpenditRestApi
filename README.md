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
-  **All Users:** Only for Test purpose, Anyone can get  the all users as a JSON
-  **All Tokens:** Only for Test purpose, Anyone can get  the all tokens as a JSON


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
        

### Git

