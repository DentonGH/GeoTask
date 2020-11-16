package com.cagriyorguner.geotask.Models;

import com.cagriyorguner.geotask.Models.ApiResponseModels.Job;
import com.cagriyorguner.geotask.Models.ApiResponseModels.Vehicle;

import java.util.List;

public class PostModel {
    List<Job> jobs;
    List<Vehicle> vehicles;

    public PostModel(List<Job> jobs, List<Vehicle> vehicles) {
        this.jobs = jobs;
        this.vehicles = vehicles;
    }

    //getters and setters

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
