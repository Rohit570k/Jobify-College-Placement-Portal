import Job from '../models/Job.js';
import User from "../models/User.js";
import Academics from '../models/Academics.js';

import { StatusCodes } from "http-status-codes";
import {BadRequestError,UnAuthenticatedError,NotFoundError} from '../errors/index.js';
import {checkPermissions,onlyAdminPermission} from '../utils/checkPermission.js';
import mongoose from 'mongoose';
import moment from 'moment';

const create = async (req,res)=>{
//     const {  } = req.body;

//   if ( true) {
//     throw new BadRequestError('Please provide all values');
//   }

  const alreadyCreated = await Academics.findOne({createdBy: req.user.userId});
    if(alreadyCreated){
        throw new BadRequestError(`User already created `)
    }

  req.body.createdBy = req.user.userId;
  const academics = await Academics.create(req.body);
  res.status(StatusCodes.CREATED).json({ academics });
}

const getMyAcademics = async (req,res) =>{
    const academics = await Academics.findOne({createdBy: req.user.userId});
    res
    .status(StatusCodes.OK)
    .json({academics });

}

export {create,getMyAcademics}