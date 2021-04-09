package org.me.gcu.equakestartercode;
//Filip Chojnacki S1712859
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        String val = intent.getStringExtra("Title");

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(val);
    }
}