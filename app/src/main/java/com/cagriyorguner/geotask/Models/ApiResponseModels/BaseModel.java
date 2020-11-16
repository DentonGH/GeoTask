package com.cagriyorguner.geotask.Models.ApiResponseModels;

import java.util.List;

public class BaseModel {
    int code;
    Summary summary;
    List<Integer> unassigned;
    List<Route> routes;

    //getters and setters

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<Integer> getUnassigned() {
        return unassigned;
    }

    public void setUnassigned(List<Integer> unassigned) {
        this.unassigned = unassigned;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
