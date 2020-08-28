package com.just.linedemo.bean;

import java.util.List;

/**
 * <pre>
 * 作者 : hyf
 * 公司: 广州佳时达软件股份有限公司
 * 时间 : 2020-08-27
 * </pre>
 */
public class DataLineSet {
    private int color;
    private  List<Entry> entryList;

    public DataLineSet() {
    }

    public DataLineSet(int color, List<Entry> entryList) {
        this.color = color;
        this.entryList = entryList;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<Entry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<Entry> entryList) {
        this.entryList = entryList;
    }
}
