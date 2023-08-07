import express from 'express'

const router = express.Router()


import {createApplication,getMyApplication,showMyAppliedStats,getApplicationByJob,getSelectedStudent,updateApplication} from '../controllers/applicationController.js'

//@all are private authenticate middleware in server.js
router.route('/').post(createApplication).get(getMyApplication)
router.route('/stats').get(showMyAppliedStats)
router.route('/selected/:jobId').get(getSelectedStudent)
//For TPO
router.route('/:id').get(getApplicationByJob)
router.route('/updateStatus').post(updateApplication)

export default router