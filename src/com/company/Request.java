package com.company;

public class Request {

    public int startDate;
    public int endDate;

    public Request(int startDate, int endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "(" + this.startDate + "," + this.endDate + ")";
    }
}
