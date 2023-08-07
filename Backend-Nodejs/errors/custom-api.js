import {  StatusCodes } from "http-status-codes";

// extends error and creating constructor so that can also send statusCode
class CustomAPIError extends Error {
    constructor(message){
        super(message)
    }
}

export default CustomAPIError;

