package amrit.com.gurbaniprayers;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    Button buttons[] = new Button[8];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttons[0] = (Button) findViewById(R.id.japjisahib);
        buttons[1] = (Button) findViewById(R.id.jaapsahib);
        buttons[2] = (Button) findViewById(R.id.tavparsadsavaiye);
        buttons[3] = (Button) findViewById(R.id.chaupaisahib);

        buttons[4] = (Button) findViewById(R.id.anandsahib);
        buttons[5] = (Button) findViewById(R.id.rehraassahib);
        buttons[6] = (Button) findViewById(R.id.kirtansohaila);
        buttons[7] = (Button) findViewById(R.id.sukhmanisahib);

        for(int i=0; i<buttons.length; i++){
            Typeface typeBold = Typeface.createFromAsset(getAssets(),"font/Gurakh_h.ttf");
            buttons[i].setTypeface(typeBold);
            buttons[i].setTransformationMethod(null);
        }
    }

    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, PaathActivity.class);
        Bundle b = new Bundle();

        switch (v.getId()) {
            case R.id.japjisahib: b.putString("value", "japjisahib"); break;
            case R.id.jaapsahib: b.putString("value", "jaapsahib"); break;
            case R.id.tavparsadsavaiye: b.putString("value", "tavparsadsavaiye"); break;
            case R.id.chaupaisahib: b.putString("value", "chaupaisahib"); break;

            case R.id.anandsahib: b.putString("value", "anandsahib"); break;
            case R.id.rehraassahib: b.putString("value", "rehraassahib"); break;
            case R.id.kirtansohaila: b.putString("value", "kirtansohaila"); break;
            case R.id.sukhmanisahib: b.putString("value", "sukhmanisahib"); break;
        }
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
//        finish();
    }
}
