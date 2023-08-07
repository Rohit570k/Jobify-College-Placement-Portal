import { UnAuthenticatedError } from "../errors/index.js";
import User from "../models/User.js";

const checkPermissions= (requestUser,resourceUserId) =>{
    //if(requestUser.role === 'Admin') return ;
    if(requestUser.userId === resourceUserId.toString()) return;

    throw new UnAuthenticatedError("Not authorized to access this route")
}

const onlyAdminPermission =  (requestUser)=>{
    
    if(requestUser.role ==='TPO') return;

   throw new UnAuthenticatedError("Not authorized person to access this route")
    
}
export {checkPermissions,onlyAdminPermission}