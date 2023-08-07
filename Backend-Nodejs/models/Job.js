import mongoose from "mongoose";

const JobSchema = new mongoose.Schema({
    companyImg: {
        type: String,
        required:false,
        default: 'https://th.bing.com/th/id/OIP.pZ6Aq0XiPoOS7y6vLY0YMgAAAA?pid=ImgDet&rs=1'
    },
    company: {
        type:String ,
        required: [true,'Please provide company'],
        maxLength: 20,
    },
    position: {
        type:String ,
        required: [true,'Please provide position'],
        maxLength: 100,
    },
    status: {
        type:String ,
        enum: ['ongoing','completed','upcoming'],
        default:'ongoing',
    },
    jobType: {
        type:String,
        enum: ['fulltime','parttime','remote','internship'],
        default: 'fulltime',
    },
    jobLocation: {
        type:String,
        default:'my city',
        required: true,
    },
    jobDescription: {
        type:String,
        default:'We are hiring',
    },
    jobCTC: {
        type:String,
        required: true,
    },
    createdBy: {
        type:mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: [true , 'Please provide user']
    },

 },
 { timestamps:true }

)


export default mongoose.model('Job',JobSchema)