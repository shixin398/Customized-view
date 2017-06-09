package com.example.spreadtrumshitaoli.emojiwhat;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class MainActivity extends Activity {

    private String TAG = "Emoji";
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.et);

        doListen(mEditText);
    }

    private void doListen(EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG,"beforeTextChanged text: "+s+",start: "+start+", count: "+count+", after: "+after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG,"onTextChanged text: "+s+",start: "+start+", count: "+count+", before: "+before);
                for (int i=0; i<s.length();i++) {
                    Log.d(TAG,"text i:"+i+" is "+s.charAt(i));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG,"afterTextChanged text: "+s);
            }
        });

    }
}
