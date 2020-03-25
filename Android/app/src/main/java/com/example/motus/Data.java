package com.example.motus;

public class Data {
    private String Time;
    private  String Angle;

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    private String Uid;
    public Data(){}
    public  Data(String time, String angle, String uid){
    Time = time;
    Angle = angle;
    Uid = uid;
    }

    public String getTime() {
        return Time;
    }

    public String getAngle() {
        return Angle;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setAngle(String angle) {
        Angle = angle;
    }
}
