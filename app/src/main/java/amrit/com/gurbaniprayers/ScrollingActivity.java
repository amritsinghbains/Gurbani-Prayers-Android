package amrit.com.gurbaniprayers;

import android.app.ListActivity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ScrollingActivity extends AppCompatActivity {

    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    ListView listView;

    List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    MediaPlayer m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list2);
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        final Typeface typeBold = Typeface.createFromAsset(getAssets(),"font/Gurakh_h.ttf");

        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[]{"title",
                "subtitle" }, new int[] { android.R.id.text1,
                android.R.id.text2 }){
            @Override
            public View getView(int pos, View convertView, ViewGroup parent){
                View v = convertView;
                if(v== null){
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v=vi.inflate(android.R.layout.simple_list_item_2, null);
                }
                TextView tv = (TextView)v.findViewById(android.R.id.text1);
                tv.setText(data.get(pos).get("title"));
                TextView tv2 = (TextView)v.findViewById(android.R.id.text2);
                tv2.setText(data.get(pos).get("subtitle"));
                tv.setTypeface(typeBold);
                return v;
            }


        };

//        SimpleAdapter adapter = new SimpleAdapter(this, data,
//                android.R.layout.simple_list_item_2,
//                new String[] {"title", "subtitle"},
//                new int[] {android.R.id.text1,
//                        android.R.id.text2});
//        this.setListAdapter(adapter);
        listView.setDivider(null);
        listView.setAdapter(adapter);

        Bundle b = getIntent().getExtras();
         // or other values
        String newValue = "";
        if(b != null) {
            final String value = b.getString("value");

            switch(value){
                case "japjisahib": newValue = "Japji Sahib"; break;
                case "jaapsahib": newValue = "Jaap Sahib"; break;
                case "tavparsadsavaiye": newValue = "Tav Parsad Savaiye"; break;
                case "chaupaisahib": newValue = "Chaupai Sahib"; break;
                case "anandsahib": newValue = "Anand Sahib"; break;
                case "rehraassahib": newValue = "Rehraas Sahib"; break;
                case "kirtansohaila": newValue = "Kirtan Sohaila"; break;
                case "sukhmanisahib": newValue = "Sukhmani Sahib"; break;
            }

            setTitle(newValue);

            int id = getResources().getIdentifier(value, "drawable", getPackageName());
//            Drawable drawable = getResources().getDrawable(id);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setImageResource(id);


            Log.d("hahaha", "Start Paath...");
            try {

                InputStreamReader is = new InputStreamReader(getAssets()
                        .open(value.concat(".csv")));

                BufferedReader reader = new BufferedReader(is);
                reader.readLine();
                String line;
                StringTokenizer st = null;

                while ((line = reader.readLine()) != null) {
//                    Log.d("hahaha", line);
                    st = new StringTokenizer(line, ",");
                    // attributes
                    addToList(st.nextToken(), st.nextToken());
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    // your code here
                                    highlightText();
                                }
                            },
                            Integer.parseInt(st.nextToken())*1000
                    );
                }
//                adapter.notifyDataSetChanged();

                if(!value.contains("sukhmanisahib")) {
//                    adjustHeightList();
                }
//                listView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(!value.contains("sukhmanisahib")) {
//                            flag = 2;
//                        }
//                    }
//                });

                if(!value.contains("sukhmanisahib")) {
                    flag = 2;
                }

                listView.setSelectionAfterHeaderView();
                listView.smoothScrollToPosition(0);

                m = new MediaPlayer();
                playBeep(value.concat(".mp3"));

            }catch(Exception e){
                Log.d("hahaha", e.getMessage());
            }


        }


    }

    public void adjustHeightList(){
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < 10; i++) {
            View listItem = listView.getAdapter().getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listView.getAdapter().getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }




    public void addToList(String english, String punjabi){
//        Log.d("hahaha", english);

        Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("title", punjabi);
            datum.put("subtitle", english);
            data.add(datum);


    }
    int counter = 6;
    int flag = 1;
    public void highlightText(){
        if(flag == 2) {
            Log.d("hahaha", "new highlighted");
            listView.smoothScrollToPosition(counter++);
//            listView.setItemChecked(counter, true);
        }
    }

    public void playBeep(String value) {
        try {
            if (m.isPlaying()) {
                m.stop();
                m.release();
                m = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = getAssets().openFd(value);
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.setLooping(true);
            m.start();
        } catch (Exception e) {
            Log.d("hahaha", e.getMessage());
        }
    }

//    public void onBackPressed() {
//        closePlayer();
//        super.onBackPressed();
//        this.finish();
//    }

    @Override
    protected void onPause() {
        closePlayer();
        super.onPause();
        finish();
    }

    void closePlayer(){
        if (m.isPlaying()) {
            m.stop();
            m.release();
        }
    }

}
