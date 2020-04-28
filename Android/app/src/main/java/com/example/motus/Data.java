package com.example.motus;

public class Data {
    private String Time;
    private  String Angle;
    //added code
    private  String Movement;

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    private String Uid;
    public Data(){}
    public  Data(String time, String angle, String uid, String movement){
        Time = time;
        Angle = angle;
        Uid = uid;

        //added code
        Movement= movement;
    }

    public String getTime() {
        return Time;
    }

    public String getAngle() {
        return Angle;
    }

    //added code
    public String getMovement(){ return  Movement;}

    public void setTime(String time) {
        Time = time;
    }

    public void setAngle(String angle) {
        Angle = angle;
    }

    //added code
    public void setMovement(String movement){Movement = movement;}
}
