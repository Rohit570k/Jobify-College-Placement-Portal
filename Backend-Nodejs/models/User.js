import mongoose from "mongoose";
import validator from "validator";
import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";

const UserSchema = new mongoose.Schema({
    name: {
        type:String ,
        required: [true,'Please provide name'],
        minLength: 3,
        maxLength: 20,
        trim : true,
    },
    email: {
        type:String ,
        required: [true,'Please provide email'],
        validate: {
            validator: validator.isEmail,
            message: 'Please provide a valid Email'
        },
        unique: true,
    },
    password: {
        type:String ,
        required: [true,'Please provide password'],
        minLength: 6, 
        select :false,
    },
    lastName: {
        type:String ,
        maxLength: 20,
        default: 'lastName',
        trim : true,
    },
    location: {
        type:String ,
        maxLength: 20,
        default: 'my city',
        trim : true,
    },
    role:{
        type:String,
        enum:['student','TPO'],
        default : 'student',
        required:true,
    },
    erpId:{
        type:Number,
        maxLength:10,
        default:123456
    }

})

UserSchema.pre('save',async function(next){
    //console.log(this.modifiedPaths())
    //console.log(this.isModified('name'))
    if(!this.isModified('password')) return;
    //if we are modifying pass then we reach below , works also when registring
    const salt = await bcrypt.genSalt(10);
    this.password = await bcrypt.hash(this.password,salt);
    console.log(this)
  next();
})

UserSchema.methods.createJWT = function () {
    return jwt.sign({userId:this._id},process.env.JWT_SECRET,{
        expiresIn:process.env.JWT_LIFETIME})
}

UserSchema.methods.comparePassword = async function(givenpassowrd){
    const isSame = await bcrypt.compare(givenpassowrd,this.password)
    return isSame;
}
export default mongoose.model('User',UserSchema)