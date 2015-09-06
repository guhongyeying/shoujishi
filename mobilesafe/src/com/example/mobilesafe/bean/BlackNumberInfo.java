package com.example.mobilesafe.bean;

/**
 * Created by wangren on 15/8/27.
 */
public class BlackNumberInfo {
    private String number;
    private String mode;

    public BlackNumberInfo(String mode, String number) {
        this.mode = mode;
        this.number = number;
    }

    public BlackNumberInfo() {

    }

    @Override
    public String toString() {
        return "BlackNumberInfo{" +
                "number='" + number + '\'' +
                ", mode='" + mode + '\'' +
                '}';
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
