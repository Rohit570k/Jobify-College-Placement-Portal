import mongoose from "mongoose";

const AcademicsSchema = new mongoose.Schema({
    // name
    // collegename
    // rollnos
    // erpid
    // 10thmark
    // 10yearofpassing
    // 12thmark
    // 12thyearof passing
    // college%
    // collegeyearofpassing
    //title


    name: {
        type:String ,
        required: [true,'Please provide name'],
        maxLength: 20,
    },
    college: {
        type:String ,
        required: [true,'Please provide collegename'],
    },
    rollnos: {
        type:String ,
        required: [true,'Please provide rollnos'],
        maxLength: 20,
    },
    tenthmark: {
        type:String ,
        required: [true,'Please provide passing year'],
    },
    tenthyearofpassing: {
        type:String ,
        required: [true,'Please provide passing year'],
    },
    twelfthmark: {
        type:String ,
        required: [true,'Please provide 12th mark'],
        maxLength: 20,
    },
    twelfthmarkyearofpassing: {
        type:String ,
        required: [true,'Please provide passing year'],
    },
    collegemarks: {
        type:String ,
        required: [true,'Please provide college mark'],
        maxLength: 20,
    },
    collegeyearofpassing: {
        type:String ,
        required: [true,'Please provide passing year'],
        maxLength: 20,
    },
    nosofbacklog:{
        type:String,
        required:[true,'Provide backlog nos'],
    },
    title: {
        type:String ,
        required: [true,'Please provide title'],
        maxLength: 20,
    },
    createdBy: {
        type:mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: [true , 'Please provide user']
    },

 }

)


export default mongoose.model('Academics',AcademicsSchema)