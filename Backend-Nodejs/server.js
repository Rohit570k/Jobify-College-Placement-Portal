import express from 'express'
const app = express()
import dotenv from 'dotenv'
dotenv.config()

import 'express-async-errors'
import morgan from 'morgan'
//db and authenticateUser
import connectDB from './db/connect.js'

// routers
import authRouter from "./routes/authRoutes.js";
import jobRouter from "./routes/jobsRoutes.js";
import applicationRouter from "./routes/applicationRoutes.js";
import academicsRouter from "./routes/academicsRoutes.js"

//middleware
import notFoundMiddleware from './middleware/not-found.js'
import errorHandlerMiddleware from './middleware/error-handler.js'
import authenticateUser from './middleware/authenticate.js'

import cors from 'cors';
const corsOptions ={
   origin:'*', 
   credentials:true,            //access-control-allow-credentials:true
   optionSuccessStatus:200,
}


if (process.env.NODE_ENV !== 'production') {
    app.use(morgan('dev'));
  }

app.use(express.json()) //built in middleware from express, json data will available to us in controller
app.use(cors(corsOptions)) // Use this after the variable declaration


app.get('/',(req,res)=>{
    //throw new Error('my error')
    res.send("Hello to cllg project")
})

app.use('/api/v1/auth',authRouter)
app.use('/api/v1/jobs',authenticateUser,jobRouter)
app.use('/api/v1/application',authenticateUser, applicationRouter)
app.use('/api/v1/academics', authenticateUser, academicsRouter)

//First sever will match every route up and not found then move below 
app.use(notFoundMiddleware)

app.use(errorHandlerMiddleware)




const port = process.env.PORT || 5000



const start = async () => {
    try{
        await connectDB(process.env.MONGO_URL)
       
        app.listen(port,()=>{
            console.log(`Server is listening on port ${port}..`)
        })
    } catch(error){
        console.log(error)
    }
}

start()