## 1. Setup Server

- start setting up our server
- setup package.json
```
npm init -y
```

## 2. ES6 vs CommonJS
```
CommonJS:-
const express = require('express');
const app = express();

ES6 :-

import express from 'express';
const app = express();
```
**Write below code inside `package.json` to use es6 module inside server**

```
"type":"module"
```
## 3. Nodemon and Basic Express Server
```
npm install nodemon --save-dev
npm install express
```
**Write below code inside `package.json` for nodemon inside script**
```
"start":"nodemon server"
```
**Basic express server code eg:**
```
import express from 'express';
const app = express();

app.get('/', (req, res) => {
  res.send('Welcome!');
});

const port =  5000;

app.listen(port, () => console.log(`Server is listening on port ${port}...`));
```
## 4.ENV Variables
```sh
npm install dotenv
```
create `.env` and write
```
PORT=4000
```

inside `server.js`
```
import dotenv from 'dotenv'
dotenv.config()
```

## 5.Connect to DB
```
npm install mongoose
```
- create db/connect.js
- define function to connectDB(url) returns a promise
- set connection string in `.env` by providing credential and dB name 
- in `server.js` invoke connectDb passing connection string as url 
which is inside `.env`.

## 6.Validator Package
 - external mongoose library done all those already inplace
 ```
 npm install validator
 ```
 - import inside model user.js
  ```
  validate:{
  validator:`(field)=> {return 2 > 1}`,
  message:'Please provide valid email'
  }
  ```

## 7.Use Express-Async-Errors Package
- No need to write try/catch for handling error in every controller
- This take care and pass to the error handler any error occurs
- remove try/catch
```
npm install express-async-errors
```
- inside `server.js`
```js

import 'express-async-errors'
```


## 8.[Http Status Codes](https://www.npmjs.com/package/http-status-codes)
- constants for status codes
```
npm install http-status-codes
```
- import/setup in authController and error-handler
```
import {StatusCodes} from "http-status-codes";
```

## 9. [Hash Passwords](https://www.npmjs.com/package/bcryptjs)
- to save hash password
- [About mongoose middleware](https://mongoosejs.com/docs/middleware.html)
```
npm install bcryptjs
```
**IN USER MODEL**
```js
import bcrypt from "bcryptjs"
```
```js
UserSchema.pre('save',async function(next){
    const salt = await bcrypt.genSalt(10);
    this.password = await bcrypt.hash(this.password,salt);
  next();
})
```

## 10. [JWT](https://www.npmjs.com/package/jsonwebtoken)
```
npm install jsonwebtoken
```
**USER MODEL**
```js
import jwt from 'jsonwebtoken'
```
- we need to write this for creating jwt.sign(payload,secret,options)
- to generate [secret](https://www.allkeysgenerator.com/) we can use this key generator of 256 bit
```js
UserSchema.methods.createJWT = function () {
    return jwt.sign({userId:this._id},process.env.JWT_SECRET,{
        expiresIn:process.env.JWT_LIFETIME})
}
```
- then invoke this `user.createJWT()` inside auth controller
- **To verify token**
- we send bearer token through authorization header and verify here at server
- we create a middleware and execute before every private api request
- inside "authenticated.js" middleware we verify
```js
try {
    const payload = jwt.verify(token,process.env.JWT_SECRET);
     console.log(payload)
    next();
    } catch (error) {
        throw new UnAuthenticatedError("Authentication Invalid")
    }
    ```

## 10. [Morgan package](https://www.npmjs.com/package/morgan)
- http logger middleware for node.js
```sh
npm install morgan
```
- **Inside server js**
```js
import morgan from 'morgan';

if (process.env.NODE_ENV !== 'production') {
  app.use(morgan('dev'));
}
```

## 11. Populate in DB automatically 

- use [Mockaroo](https://www.mockaroo.com/) to create dummy data 
- save it as `mock-data.json `
- First close the server 
- Create `populate.js` in root and code for populating database
- Run populate.js with command 
```sh
node populate
```
- Dummy data will be created in database


## 12 Aggregation PipeLine
- about it [docs](https://www.mongodb.com/docs/manual/core/aggregation-pipeline/)


- [Reduce Basics](https://www.youtube.com/watch?v=3WkW9nrS2mw)
- [Reduce Object Example](https://www.youtube.com/watch?v=5BFkp8JjLEY)