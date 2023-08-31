import mongoose from "mongoose";

// Mongoose connect method return promise so we use async await
const connectDB = (url) =>{
    const connected=  mongoose.connect(url)

    return  connected
    
}

export default connectDB;