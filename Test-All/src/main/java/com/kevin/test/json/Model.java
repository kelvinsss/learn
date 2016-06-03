package com.kevin.test.json;

/**
 * Created by Administrator on 2016/6/1.
 */
public class Model {


    private Object date;

    private String da;


    public Model(Object date, String da) {
        this.date = date;
        this.da = da;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }
}
