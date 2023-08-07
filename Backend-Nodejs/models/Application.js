import mongoose from "mongoose";

const ApplicationSchema = new mongoose.Schema({
    
    applicant: {
        type:mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: [true , 'Please provide user']
    },
    applied: {
        type:mongoose.Schema.Types.ObjectId,
        ref: 'Job',
        required: [true , 'applied in which job']
    },
    company: {
        type:String ,
        required: [true,'Please provide company'],
        maxLength: 20,
    },
    status: {
        type:String ,
        enum: ['firstround','secondround','interview','selected'],
        default:'firstround',
    },
    
 },
 { timestamps:true }

)


export default mongoose.model('Application',ApplicationSchema)