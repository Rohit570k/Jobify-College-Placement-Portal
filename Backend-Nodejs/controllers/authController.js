import User from '../models/User.js'
import {StatusCodes} from "http-status-codes";
import { readFile } from "fs/promises";

import {BadRequestError,UnAuthenticatedError} from '../errors/index.js';
const register = async (req,res) =>{
    // res.send('register user')
    const {name, email,password} = req.body

    if(!name||!email||!password){
        //As statuscode will be send , as in Default Error class we dont have any property for statuscode
        throw new BadRequestError('Please provide all values') 
    }

    const userAlreadyExist = await User.findOne({email});
    if(userAlreadyExist){
        throw new BadRequestError(`Email ${email} is already registered`)
    }
  

    //using create it call the pre middleware, there we will hash password
    //before saving
    const user = await User.create(req.body);
    console.log("user   ", user)
    user.password = undefined
    const userres = {email:user.email,
                    name:user.name,
                    location: user.location}
    const token = user.createJWT();
    res.status(StatusCodes.CREATED).json({user,token})

}

const login = async (req,res) =>{
    const {email,password} = req.body

    if(!email||!password){
        //As statuscode will be send , as in Default Error class we dont have any property for statuscode
        throw new BadRequestError('Please provide all values') 
    }
    
    const user = await User.findOne({email}).select('+password');

    if(!user){
        throw new UnAuthenticatedError("Invalid Cred email doesn't exist");
    }
    //console.log(user)
    const isPasswordCorrect = await user.comparePassword(password);
    if(!isPasswordCorrect){
        throw new UnAuthenticatedError("Invalid Cred Password incorrect")
    }
    const token = user.createJWT();
    user.password = undefined
    res.status(StatusCodes.OK).json({user , token })
};

const updateUser = async (req,res) =>{
    //console.log("requpdateuser:",req.body);
    const { name, lastName, email, location, erpId } = req.body;
    if (!email || !name || !lastName || !location || !erpId) {
      throw new BadRequestError('Please provide all values');
    }
  
    const user = await User.findOne({ _id: req.user.userId });
  
    // user.email = email;
    user.name = name;
    user.lastName = lastName;
    user.location = location;
    user.erpId = erpId;
  
    await user.save();
  
    // various setups
    // in this case only id
    // if other properties included, must re-generate
  
    const token = user.createJWT();
    res.status(StatusCodes.OK).json({
      user,
      token,
    });
};
    const getActor =async(req,res)=>{
        const jsonProducts = JSON.parse(
            await readFile(new URL('../actor.json', import.meta.url))
          );
          console.log(jsonProducts)
          res.status(StatusCodes.OK)
          .json(jsonProducts)
    }


export {register,login,updateUser,getActor}
//eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2NDVlMmQwOGY3NWQ3NjI1MTZlNTI2NjEiLCJpYXQiOjE2ODQxMTAzOTMsImV4cCI6MTY4NDE5Njc5M30.TQepqMSw7RhQwTylTWil9FEknqpEBUE3qEVmCSAg3Sg