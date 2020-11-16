package com.cagriyorguner.geotask.Models.ApiResponseModels;

import java.util.List;

public class Job {
    int id;
    int service;
    List<Integer> amount;
    List<Double> location;
    List<Integer> skills;
    List<List<Integer>> time_windows;

    public Job(int id, int service, List<Integer> amount, List<Double> location, List<Integer> skills, List<List<Integer>> time_windows) {
        this.id = id;
        this.service = service;
        this.amount = amount;
        this.location = location;
        this.skills = skills;
        this.time_windows = time_windows;
    }

    //getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public List<Integer> getAmount() {
        return amount;
    }

    public void setAmount(List<Integer> amount) {
        this.amount = amount;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public List<Integer> getSkills() {
        return skills;
    }

    public void setSkills(List<Integer> skills) {
        this.skills = skills;
    }

    public List<List<Integer>> getTime_windows() {
        return time_windows;
    }

    public void setTime_windows(List<List<Integer>> time_windows) {
        this.time_windows = time_windows;
    }
}
