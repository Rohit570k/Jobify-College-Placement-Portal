import express from 'express'
import authenticateUser from '../middleware/authenticate.js'

const router = express.Router()


import {register,login,updateUser,getActor} from '../controllers/authController.js'
//@Public 
router.route('/register').post(register);
router.route('/login').post(login);
//@Private 
router.route('/updateUser').patch(authenticateUser, updateUser);
router.route('/actor').get(getActor)


export default router