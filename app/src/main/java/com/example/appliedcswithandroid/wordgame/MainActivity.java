package com.example.appliedcswithandroid.wordgame;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Word Game";
    Node root = null;
    Random random;
    ArrayList<Character> list = null;
    GridView gridView;
    Button wordFormed, reset;
    TextView score;

    MyAdapter adapter = null;

    String wordEntered = "";

    private static int WORD_COUNT = 0;

    String s;
    char[] startingLetters = new char[16];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        random = new Random();
        gridView = (GridView) findViewById(R.id.gridview);

        score = (TextView) findViewById(R.id.score);
        score.setText("Score: " + WORD_COUNT);

        wordFormed = (Button) findViewById(R.id.word_formed);
        reset = (Button) findViewById(R.id.reset_button);

        InputStream inputStream = null;
        AssetManager assetManager = getAssets();
        try {
            inputStream = assetManager.open("words.txt");

        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        //For inserting the dictionary words in a data Structure
        try {
            while((line = in.readLine()) != null)
            {
                Node curr = new Node(line.length(),new ArrayList<String>());
                curr.arrayList.add(line);
                if(root==null)
                    root = curr;
                else
                    Insert_Node(root,curr, line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

       // Log.d(TAG, "Dictionary Formed");
/*
        String s = getWord(4,root) + getWord(4,root) + getWord(5,root) + getWord(3,root);

        char[] startingLetters = new char[16];
        startingLetters = s.toUpperCase().toCharArray();

        list = new ArrayList<Character>();
        for(int i=0;i<16;i++)
            list.add(startingLetters[i]);

        Collections.shuffle(list);
        gridView.setAdapter(new MyAdapter(this, list));
*/
        reset_grid();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                char letter = list.get(position);

                wordEntered += letter;

                wordFormed.setText(wordEntered);

    //            gridView.getAdapter().getItem(position);
            }
        });

        wordFormed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wordEntered.length() < 3) {
                    Toast.makeText(MainActivity.this, "The word must be of size greater than 2", Toast.LENGTH_SHORT).show();
                    wordEntered = "";
                    wordFormed.setText(wordEntered);
                }
                else
                    check(wordEntered);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_grid();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getWord(int size,Node curr)
    {
        System.out.println(curr.arrayList.get(0));
        if(curr.size==size)
            return curr.arrayList.get(random.nextInt(curr.arrayList.size()));
        else if(curr.size>size)
            return getWord(size, curr.left);
        else
            return getWord(size, curr.right);

    }

    public static boolean Search(String word,Node curr)
    {
        int size = word.length();
        if(curr==null)
            return false;

        if(curr.size==size)
        {
            if(curr.arrayList.contains(word))
                return  true;
            else
                return false;
        }
        else if(curr.size>size)
            return Search(word, curr.left);
        else
            return Search(word, curr.right);

    }

    public void Insert_Node(Node currRoot,Node curr,String word)
    {
        int size = word.length();
        if(currRoot.size==size)
            currRoot.arrayList.add(word);
        else if(currRoot.size>size)
        {
            if(currRoot.left==null)
                currRoot.left = curr;
            else
                Insert_Node(currRoot.left,curr,word);
        }
        else
        {
            if(currRoot.right==null)
                currRoot.right = curr;
            else
                Insert_Node(currRoot.right,curr, word);
        }
    }

    public void check(String word)
    {
        word = word.toLowerCase();

        if(!Search(word,root)) {
            Toast.makeText(this.getApplicationContext(), "The word you entered is not valid", Toast.LENGTH_SHORT).show();
            wordEntered = "";
            wordFormed.setText(wordEntered);
        }
        else
        {
            WORD_COUNT++;
            score.setText("Score: " + WORD_COUNT);

            for(int i=0;i<word.length();i++)
            {
                list.remove((Object) word.charAt(i));
            }

            String newWord = this.getWord(word.length(),root).toUpperCase();

            for(int i=0;i<newWord.length();i++)
                list.add(newWord.charAt(i));

            Collections.shuffle(list);
            adapter.notifyDataSetChanged();

            wordEntered = "";
            wordFormed.setText(wordEntered);
        }
    }

    public void reset_grid() {
        WORD_COUNT = 0;

        wordEntered = "";
        wordFormed.setText(wordEntered);

        score.setText("Score: " + WORD_COUNT);

        s = getWord(4,root) + getWord(4,root) + getWord(5,root) + getWord(3,root);

        startingLetters = s.toUpperCase().toCharArray();

        if (list == null)
            list = new ArrayList<>();

        for(int i=0;i<16;i++)
            list.add(startingLetters[i]);

        Collections.shuffle(list);

        if(adapter == null) {
            adapter = new MyAdapter(this, list);
            gridView.setAdapter(adapter);
        }
        else
            adapter.notifyDataSetChanged();
    }
}

class Node {
    int size;
    ArrayList<String> arrayList;
    Node right;
    Node left;

    Node(int size, ArrayList<String> arrayList) {
        this.size = size;
        this.arrayList = arrayList;
        right = null;
        left = null;
    }
}