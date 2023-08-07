import express from 'express'

const router = express.Router()


import {create,getMyAcademics} from '../controllers/academicsController.js'

//@all are private authenticate middleware in server.js
router.route('/').post(create).get(getMyAcademics)



export default router