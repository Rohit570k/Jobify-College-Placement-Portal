import mongoose from "mongoose";

// Moongoose connect method return promise so we use async await
const connectDB = (url) =>{
    const connected=  mongoose.connect(url)
    console.log("DB is connected ")
    return  connected
    
}

export default connectDB;