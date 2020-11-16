package com.cagriyorguner.geotask.Models.ApiResponseModels;

import java.util.List;

public class Step {
    String type;
    List<Double> location;
    int id;
    int service;
    int waiting_time;
    int job;
    List<Integer> load;
    int arrival;
    int duration;

    //getters and setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

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

    public int getWaiting_time() {
        return waiting_time;
    }

    public void setWaiting_time(int waiting_time) {
        this.waiting_time = waiting_time;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public List<Integer> getLoad() {
        return load;
    }

    public void setLoad(List<Integer> load) {
        this.load = load;
    }

    public int getArrival() {
        return arrival;
    }

    public void setArrival(int arrival) {
        this.arrival = arrival;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
