package com.just.linedemo.bean;

/**
 * <pre>
 * 作者 : hyf
 * 公司: 广州佳时达软件股份有限公司
 * 时间 : 2020-08-21
 * </pre>
 */
public class Entry {
    private String yValue;
    private float xValue;
    private int status=0;

    public Entry(float xValue,String yValue) {
        this.yValue = yValue;
        this.xValue = xValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getyValue() {
        return yValue;
    }

    public void setyValue(String yValue) {
        this.yValue = yValue;
    }

    public float getxValue() {
        return xValue;
    }

    public void setxValue(float xValue) {
        this.xValue = xValue;
    }
}
