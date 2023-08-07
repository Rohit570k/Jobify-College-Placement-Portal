package com.example.jobify.Models;

public class AppliedApplication {
        private String applicant;
        private  Job applied;
        private String company;
        private String status;
        private String createdAt;
        private String updatedAt;

        public String getApplicant() {
            return applicant;
        }

        public void setApplicant(String applicant) {
            this.applicant = applicant;
        }

        public Job getApplied() {
            return applied;
        }

        public void setApplied(Job applied) {
            this.applied = applied;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String  updatedAt) {
            this.updatedAt = updatedAt;
        }


    }

