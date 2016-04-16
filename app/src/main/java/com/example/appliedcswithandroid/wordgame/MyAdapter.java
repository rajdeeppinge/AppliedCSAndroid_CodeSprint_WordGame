package com.example.appliedcswithandroid.wordgame;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by root on 4/16/16.
 */
public class MyAdapter extends BaseAdapter {

    private Context context;
    Random random = new Random();

    private String[] texts = { Character.toString((char) (random.nextInt(26)+'a')), Character.toString((char) (random.nextInt(26)+'a')),
            Character.toString((char) (random.nextInt(26)+'a')), Character.toString((char) (random.nextInt(26)+'a')),
            Character.toString((char) (random.nextInt(26)+'a')), Character.toString((char) (random.nextInt(26)+'a')),
            Character.toString((char) (random.nextInt(26)+'a')), Character.toString((char) (random.nextInt(26)+'a')),
            Character.toString((char) (random.nextInt(26)+'a')), Character.toString((char) (random.nextInt(26)+'a')),
            Character.toString((char) (random.nextInt(26)+'a')), Character.toString((char) (random.nextInt(26)+'a')),
            Character.toString((char) (random.nextInt(26)+'a')), Character.toString((char) (random.nextInt(26)+'a')),
            Character.toString((char) (random.nextInt(26)+'a')), Character.toString((char) (random.nextInt(26)+'a')),};

    public MyAdapter(Context context) {
        this.context = context;
    }

    public int getCount() {
        return 16;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv;
        if (convertView == null) {
            tv = new TextView(context);
            tv.setLayoutParams(new GridView.LayoutParams(125, 125));
        }
        else {
            tv = (TextView) convertView;
        }

        tv.setTextSize(25.0f);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setText(texts[position]);

        return tv;
    }
}