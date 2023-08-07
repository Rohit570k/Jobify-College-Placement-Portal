import Job from '../models/Job.js';
import User from "../models/User.js";

import { StatusCodes } from "http-status-codes";
import {BadRequestError,UnAuthenticatedError,NotFoundError} from '../errors/index.js';
import {checkPermissions,onlyAdminPermission} from '../utils/checkPermission.js';
import mongoose from 'mongoose';
import moment from 'moment';

const createJob = async (req,res)=>{
    const { position, company,jobCTC } = req.body;

  if (!position || !company|| !jobCTC) {
    throw new BadRequestError('Please provide all values');
  }
  //If user is Admin hen only he can create job
  const user = await User.findOne({_id: req.user.userId})
  onlyAdminPermission(user);
 
  req.body.createdBy = req.user.userId;
  const job = await Job.create(req.body);
  res.status(StatusCodes.CREATED).json({ job });
}



const getAllJobs = async (req,res)=>{
  const { jobType, sort, search ,status} = req.query;

  const queryObject ={
    
  };
  if (status ) {
    queryObject.status = status;
  }else{
    queryObject.status = 'ongoing';
  }
  // add stuff based on condition
  if (jobType && jobType !== 'all') {
    queryObject.jobType = jobType;
  }
  if (search) {
    queryObject.company = { $regex: search, $options: 'i' };
  }
  // NO AWAIT
  let result = Job.find(queryObject);

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

  const jobs = await result
    res
    .status(StatusCodes.OK)
    .json({jobs , totalJobs: jobs.length, numOfPages: 1});
}




const updateJob = async (req,res)=>{
    const { id: jobId } = req.params;
  const { company, position,jobCTC } = req.body;

  if (!position || !company, !jobCTC) {
    throw new BadRequestError('Please provide all values');
  }
  const job = await Job.findOne({ _id: jobId });

  if (!job) {
    throw new NotFoundError(`No job with id :${jobId}`);
  }
  //We check permission so that different user cant update others job  
  console.log(typeof req.user.userId);// String type
  console.log(typeof job.createdBy) // Object type

  checkPermissions(req.user, job.createdBy);

  const updatedJob = await Job.findOneAndUpdate({_id:jobId}, req.body,{
    new:true,
    runValidators:true,
  })

  res.status(StatusCodes.OK)
  .json({updatedJob})
}




const deleteJob = async (req,res)=>{
    const { id:jobId} = req.params;
    
    const job = await Job.findOne({ _id: jobId });

  if (!job) {
    throw new NotFoundError(`No job with id :${jobId}`);
  }
  //We check permission so that different user cant update others job  
  checkPermissions(req.user, job.createdBy);

  await job.deleteOne()

  res.status(StatusCodes.OK)
  .json({msg: "Job Successfully removed"})
  
}



const showStats = async (req,res)=>{
  // const objectId = new mongoose.Types.ObjectId("645e2d08f75d762516e52661");
  // console.log(objectId , " type ", typeof objectId)
  //  stats = await Job.find({createdBy:req.user.userId})
  //  .populate('createdBy')
  let stats = await Job.aggregate([
    {$group: {_id: '$status' , count: { $sum: 1 } } }
  ])
  /** stats object after aggregrate
  "stats": [
    {
        "_id": "pending",
        "count": 9
    },
    {
        "_id": "interview",
        "count": 7
    },
    {
        "_id": "declined",
        "count": 15
    }
]*/
   stats = stats.reduce((acc, curr )=>{
    const {_id:title,count } = curr;
    acc[title] = count;
    return acc;
   },{})
   /**stats after using .reduce()
    * "stats": {
        "interview": 7,
        "pending": 9,
        "declined": 15
    }
    */
   const defaultStats = {
    ongoing: stats.ongoing||0,
    completed: stats.completed||0,
    upcoming: stats.upcoming || 0
   };

    //TO Filter and showcase all type of jobType
    let jobType = await Job.aggregate([
      //{$match: { createdBy: new mongoose.Types.ObjectId(req.user.userId)}},
      {$group: {_id: '$jobType' , count: { $sum: 1 } } }
    ])
    jobType = jobType.reduce((acc, curr )=>{
      const {_id:title,count } = curr;
      acc[title] = count;
      return acc;
     },{})
     const defaultJobType = {
      fulltime: jobType.fulltime||0,
      parttime: jobType.parttime||0,
      remote: jobType.remote||0,
      internship: jobType.internship || 0
     };

  //Filtering a/q to the years n month
  let monthlyApplications = await Job.aggregate([
    //{$match: {createdBy: new mongoose.Types.ObjectId(req.user.userId)}},
    {$group : {_id: {
                    year :{ $year:'$createdAt'},
                    month: {$month : '$createdAt'}
                  },
              count: {$sum: 1}   
              },
    },
    {$sort : {'_id.year':-1, '_id.month':-1}},
     {$limit : 5}
  ])
  /**monthlyApplications now
   * [...{
            "_id": {
                "year": 2021,
                "month": 7
            },
            "count": 1
        }
      ]*/
    
    monthlyApplications =monthlyApplications.map((item)=>{
      //const { _id:{year,month},count} =item
       const {count } = item;
      const date = moment()
            .month(item._id.month-1)// as mongodb->{1-12}, moment->(0-11)
            .year(item._id.year)
            .format('MMMM Y');
      return {date ,count}
    }).reverse()
    /** now monthlyApplications 
     *[... {
            "date": "September 2022",
            "count": 2
        },
      ]
     */

  res.status(StatusCodes.OK)
  .json({ status:  defaultStats,
          jobTypes: defaultJobType,
          monthlyApplications})
    // res.send('show stats job')
}

export {createJob,getAllJobs,deleteJob,updateJob,showStats}