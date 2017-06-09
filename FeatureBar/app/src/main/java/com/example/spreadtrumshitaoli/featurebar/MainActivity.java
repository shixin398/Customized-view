package com.example.spreadtrumshitaoli.featurebar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FeatureBar featureBar = new FeatureBar(this);
        Button option = featureBar.getOptionButton();
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"HHH",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
