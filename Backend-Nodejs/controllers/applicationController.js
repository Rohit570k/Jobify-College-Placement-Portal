import Job from '../models/Job.js';
import User from "../models/User.js";
import Application from '../models/Application.js';

import { StatusCodes } from "http-status-codes";
import {BadRequestError,UnAuthenticatedError,NotFoundError} from '../errors/index.js';
import {checkPermissions,onlyAdminPermission} from '../utils/checkPermission.js';
import mongoose from 'mongoose';
import moment from 'moment';

//Student apply for job 
const createApplication = async(req,res)=>{
    const { applied } = req.body;

    if ( !applied) {
      throw new BadRequestError('Please provide all values');
    }
    //get job for company name
    const job = await Job.findOne({ _id: applied });
    if(!job){
        throw new BadRequestError('No job exist of this name')
    }
    const alreadyApplied = await Application.findOne({ applied, applicant:req.user.userId});
    if(alreadyApplied){
        throw new BadRequestError('Already Applied in this Drive')
    }
    req.body.applicant = req.user.userId;
    req.body.company = job.company;
    const application = await Application.create(req.body);
    res.status(StatusCodes.CREATED).json({ application });
}

//Particular student applied in nos of jobs 
const getMyApplication = async(req,res) => {
    //TODO query
    const{status, sort, search}= req.query;

    const queryObject ={
        applicant:req.user.userId
    };
        // add stuff based on condition
    if (status && status !== 'all') {
        queryObject.status = status;
    }
    if (search) {
        queryObject.company = { $regex: search, $options: 'i' };
    }
    let result =  Application.find(queryObject)
                            .populate('applied')

    result.sort('-status')
    
    // chain sort conditions
    if (sort === 'latest') {
        result = result.sort('-createdAt');
    }
    if (sort === 'oldest') {
        result = result.sort('createdAt');
    }
    if (sort === 'a-z') {
        result = result.sort('company');
    }
    if (sort === 'z-a') {
        result = result.sort('-company');
    }

    const myApplications = await result;
    res
    .status(StatusCodes.OK)
    .json({myApplications , totalApplication: myApplications.length});
}

//status for my 
const showMyAppliedStats = async(req,res) =>{
  
    let stats = await Application.aggregate([
        {$match: {applicant: new mongoose.Types.ObjectId(req.user.userId)}},
        {$group: { _id: '$status', count: {$sum : 1}}}
    ])
    stats = stats.reduce((acc, curr )=>{
        const {_id:title,count } = curr;
        acc[title] = count;
        return acc;
       },{})
       const defaultStats = {
        firstround: stats.firstround||0,
        secondround: stats.secondround||0,
        interview: stats.interview || 0,
        selected: stats.selected || 0
       };

       let companyName= await Application.aggregate([
        {$match: {applicant: new mongoose.Types.ObjectId(req.user.userId)}},
        {$group: { _id: '$status',
                    companyNames: { $addToSet: '$company' },
                }}
        ])
        companyName = companyName.reduce((acc, curr )=>{
            const {_id:title,companyNames } = curr;
            acc[title] = companyNames;
            return acc;
           },{})

        const defaultcompanyName = {
            firstround: companyName.firstround||[],
            secondround: companyName.secondround||[],
            interview: companyName.interview || [],
            selected: companyName.selected || []
        };

       res.status(StatusCodes.OK)
       .json({ status:  defaultStats,
            companyName :defaultcompanyName,
            })

}
//Fetched by Tpo
const getApplicationByJob = async(req,res)=>{
    const { jobId} = req.params;

    //If user is Admin hen only he can create job
    const tpo = await User.findOne({_id: req.user.userId})
     onlyAdminPermission(tpo);

    const applicants = await Application.find({applied:jobId})
                            .populate('applicant')
    res
    .status(StatusCodes.OK)
    .json({applicants , totalApplicants: applicants.length});


}

//Fetched by Tpo
const getSelectedStudent = async(req,res)=>{
    const { id:jobId} = req.params;

    const applicants = await Application.find({applied:jobId,status:'selected'})
                            .populate('applicant')
    res
    .status(StatusCodes.OK)
    .json({applicants , totalApplicants: applicants.length});


}

//This can be don by TPO only to change status
const updateApplication= async(req,res) =>{
    const { applicationId,newStatus  } = req.body; // Assuming you're sending the array of user IDs in the request body
    
    //If user is Admin hen only he can create job
    const tpo = await User.findOne({_id: req.user.userId})
    // onlyAdminPermission(tpo);
    const filter = {
      _id: { $in: applicationId }, // Use the array of users to filter the documents
    };
    const update = { status: `${newStatus}` }; // New field value to update

    const result = await Application.updateMany(filter, update,{new : true});

    res.status(StatusCodes.OK)
        .json({result})
}



export {createApplication,getMyApplication,showMyAppliedStats, getApplicationByJob,getSelectedStudent,updateApplication}