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


    name: {
        type:String ,
        required: [true,'Please provide name'],
        maxLength: 20,
    },
    college: {
        type:String ,
        required: [true,'Please provide collegename'],
        maxLength: 20,
    },
    rollnos: {
        type:String ,
        required: [true,'Please provide rollnos'],
        maxLength: 20,
    },
    tenthyearofpassing: {
        type:Number ,
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
    createdBy: {
        type:mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: [true , 'Please provide user']
    },

 }

)


export default mongoose.model('Academics',AcademicsSchema)