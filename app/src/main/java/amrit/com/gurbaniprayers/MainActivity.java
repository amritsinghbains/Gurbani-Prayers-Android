package amrit.com.gurbaniprayers;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    ImageButton buttons[] = new ImageButton[9];
    SeekBar seekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

        buttons[0] = (ImageButton) findViewById(R.id.japjisahib);
        buttons[1] = (ImageButton) findViewById(R.id.jaapsahib);
        buttons[2] = (ImageButton) findViewById(R.id.tavparsadsavaiye);
        buttons[3] = (ImageButton) findViewById(R.id.chaupaisahib);

        buttons[4] = (ImageButton) findViewById(R.id.anandsahib);
        buttons[5] = (ImageButton) findViewById(R.id.rehraassahib);
        buttons[6] = (ImageButton) findViewById(R.id.kirtansohaila);
        buttons[7] = (ImageButton) findViewById(R.id.sukhmanisahib);
        buttons[8] = (ImageButton) findViewById(R.id.playpausebutton);

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        updateSeekBar();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(m != null && fromUser){
                    m.seekTo(progress * 1000);
                }
            }
        });

//        for(int i=0; i<buttons.length; i++){
//            Typeface typeBold = Typeface.createFromAsset(getAssets(),"font/Gurakh_h.ttf");
//            buttons[i].setTypeface(typeBold);
//            buttons[i].setTransformationMethod(null);
//        }
    }

    static MediaPlayer m;

    void closePlayer(){
        if (m.isPlaying()) {
            m.stop();
            m.release();
        }
    }

    public void togglePlayer() {
        if (m.isPlaying()) {
            buttons[8].setImageResource(android.R.drawable.ic_media_play);
            m.pause();
        } else {
            buttons[8].setImageResource(android.R.drawable.ic_media_pause);
            m.start();
        }
    }

    public void playBeep(String value) {
        try {
            if(m == null){
                m = new MediaPlayer();
            }
            buttons[8].setImageResource(android.R.drawable.ic_media_pause);
            m.stop();
            m.release();
            m = new MediaPlayer();

            AssetFileDescriptor descriptor = getAssets().openFd(value);
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.setLooping(false);
            m.start();
            seekBar.setMax(m.getDuration()/1000);
        } catch (Exception e) {
            Log.d("error in playing", e.getMessage());
        }
    }

    String gurbaniSelected = "";

    private Handler mHandler = new Handler();
    public void updateSeekBar(){

        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(m != null){
                    int mCurrentPosition = m.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.japjisahib: gurbaniSelected = "japjisahib"; break;
            case R.id.jaapsahib: gurbaniSelected = "jaapsahib"; break;
            case R.id.tavparsadsavaiye: gurbaniSelected = "tavparsadsavaiye"; break;
            case R.id.chaupaisahib: gurbaniSelected = "chaupaisahib"; break;
            case R.id.anandsahib: gurbaniSelected = "anandsahib"; break;
            case R.id.rehraassahib: gurbaniSelected = "rehraassahib"; break;
            case R.id.kirtansohaila: gurbaniSelected = "kirtansohaila"; break;
            case R.id.sukhmanisahib: gurbaniSelected = "sukhmanisahib"; break;
            case R.id.playpausebutton: togglePlayer(); return;


        }

        if(gurbaniSelected.length() > 0) {
            playBeep(gurbaniSelected.concat(".mp3"));
        }
    }

}
