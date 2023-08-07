package com.example.jobify.Models;

import java.util.Date;

public class Job {
        private String _id;
        private String companyImg="https://th.bing.com/th/id/OIP.pZ6Aq0XiPoOS7y6vLY0YMgAAAA?pid=ImgDet&rs=1";
        private String company;
        private String position;
        private String status;
        private String jobType;
        private String jobLocation;
        private String jobDescription;
        private String jobCTC;
        private String createdBy;
        private String createdAt ;
        public Job() {
            // Default constructor required for Firebase Realtime Database deserialization
        }

        public Job(String companyImg, String company, String position, String status, String jobType,
                   String jobLocation, String jobDescription, String jobCTC, String createdBy) {
            this.companyImg = companyImg;
            this.company = company;
            this.position = position;
            this.status = status;
            this.jobType = jobType;
            this.jobLocation = jobLocation;
            this.jobDescription = jobDescription;
            this.jobCTC = jobCTC;
            this.createdBy = createdBy;
        }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCompanyImg() {
            return companyImg;
        }

        public void setCompanyImg(String companyImg) {
            this.companyImg = companyImg;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getJobType() {
            return jobType;
        }

        public void setJobType(String jobType) {
            this.jobType = jobType;
        }

        public String getJobLocation() {
            return jobLocation;
        }

        public void setJobLocation(String jobLocation) {
            this.jobLocation = jobLocation;
        }

        public String getJobDescription() {
            return jobDescription;
        }

        public void setJobDescription(String jobDescription) {
            this.jobDescription = jobDescription;
        }

        public String getJobCTC() {
            return jobCTC;
        }

        public void setJobCTC(String jobCTC) {
            this.jobCTC = jobCTC;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt =createdAt;
        }
}
