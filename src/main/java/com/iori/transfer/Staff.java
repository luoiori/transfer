package com.iori.transfer;

import java.util.ArrayList;

public class Staff {
    private String name;
    private ArrayList<Day> days = new ArrayList<Day>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

    public void setDayNums(int num){
        for(int i=0;i<num;i++){
            days.add(new Day());
        }
    }
}
