package com.cagriyorguner.geotask.Models.ApiResponseModels;

import java.util.List;

public class Summary {
    int cost;
    int unassigned;
    List<Integer> delivery;
    List<Integer> amount;
    List<Integer> pickup;
    int service;
    int duration;
    int waiting_time;
    computing_times computing_times;

    //getters and setters

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getUnassigned() {
        return unassigned;
    }

    public void setUnassigned(int unassigned) {
        this.unassigned = unassigned;
    }

    public List<Integer> getDelivery() {
        return delivery;
    }

    public void setDelivery(List<Integer> delivery) {
        this.delivery = delivery;
    }

    public List<Integer> getAmount() {
        return amount;
    }

    public void setAmount(List<Integer> amount) {
        this.amount = amount;
    }

    public List<Integer> getPickup() {
        return pickup;
    }

    public void setPickup(List<Integer> pickup) {
        this.pickup = pickup;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWaiting_time() {
        return waiting_time;
    }

    public void setWaiting_time(int waiting_time) {
        this.waiting_time = waiting_time;
    }

    public com.cagriyorguner.geotask.Models.ApiResponseModels.computing_times getComputing_times() {
        return computing_times;
    }

    public void setComputing_times(com.cagriyorguner.geotask.Models.ApiResponseModels.computing_times computing_times) {
        this.computing_times = computing_times;
    }
}
