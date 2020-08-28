package com.just.linedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.just.linedemo.bean.DataLineSet;
import com.just.linedemo.bean.Entry;
import com.just.linedemo.widget.MutilLinePitView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MutilLinePitView line_pit = findViewById(R.id.line_pit);
        List<DataLineSet> lineSets = new ArrayList<>();
        List<Entry> entries = new LinkedList<>();
        entries.add(new Entry(-3, "08:00"));
        entries.add(new Entry(-18, "08:30"));
        entries.add(new Entry(0, "09:00"));
        entries.add(new Entry(10, "09:30"));
        entries.add(new Entry(20, "09:30"));
        entries.add(new Entry(30, "09:30"));
        entries.add(new Entry(40, "09:30"));
        entries.add(new Entry(150, "09:30"));
        DataLineSet dataLineSet = new DataLineSet(Color.parseColor("#999999"), entries);


        List<Entry> entries2 = new LinkedList<>();
        entries2.add(new Entry(-25, "08:00"));
        entries2.add(new Entry(-8, "08:30"));
        entries2.add(new Entry(2, "09:00"));
        entries2.add(new Entry(6, "09:30"));
        entries2.add(new Entry(14, "09:30"));
        entries2.add(new Entry(16, "09:30"));
        entries2.add(new Entry(0, "09:30"));
        entries2.add(new Entry(50, "09:30"));


        List<Entry> entries3 = new LinkedList<>();
        entries3.add(new Entry(-40, "08:00"));
        entries3.add(new Entry(-3, "08:30"));
        entries3.add(new Entry(8, "09:00"));
        entries3.add(new Entry(0, "09:30"));
        entries3.add(new Entry(10, "09:30"));
        entries3.add(new Entry(16, "09:30"));
        entries3.add(new Entry(2, "09:30"));
        entries3.add(new Entry(21, "09:30"));
        entries3.add(new Entry(32, "09:30"));



        DataLineSet dataLineSet2 = new DataLineSet(Color.parseColor("#ffffff"), entries2);
        DataLineSet dataLineSet3 = new DataLineSet(Color.parseColor("#ff0000"), entries3);
        lineSets.add(dataLineSet);
        lineSets.add(dataLineSet2);
        lineSets.add(dataLineSet3);


        line_pit.setMultiData(lineSets);

    }
}