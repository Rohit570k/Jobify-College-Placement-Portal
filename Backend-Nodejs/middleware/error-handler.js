import {StatusCodes} from "http-status-codes";


const errorHandlerMiddleware = (err,req,res,next)=>{
    console.log("In errormiddleware: " ,err)
    const defaultError ={
        statusCode : err.statusCode|| StatusCodes.INTERNAL_SERVER_ERROR,
        msg: err.message|| 'Something went wrong please try again later',
    }

    //Below are the database error handler 
    //Missing field error
    if(err.name==='ValidationError'){
        defaultError.statusCode=StatusCodes.BAD_REQUEST
        // defaultError.msg= err.message

        /**Object.value return an array of given objects**/
        defaultError.msg = Object.values(err.errors)
        .map((error)=>error.message)
        .join(',')
    }
    //Unique field error
    if(err.code && err.code === 11000){
        defaultError.statusCode=StatusCodes.BAD_REQUEST
        defaultError.msg = `${Object.keys(err.keyValue)} already exist has to be unique`
    }
    // res.status(defaultError.statusCode).json({msg: err})
    res.status(defaultError.statusCode).json({msg: defaultError.msg})
}

export default errorHandlerMiddleware