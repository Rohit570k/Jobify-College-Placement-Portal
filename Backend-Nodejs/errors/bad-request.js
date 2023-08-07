import { StatusCodes  } from "http-status-codes";
import CustomAPIError from "./custom-api.js";

class  BadRequestError extends CustomAPIError{
    constructor(message){
        super(message)
        //As statuscode will be send , as in Default Error class we dont have any property for statuscode
        this.statusCode = StatusCodes.BAD_REQUEST
    }
}

export default BadRequestError;