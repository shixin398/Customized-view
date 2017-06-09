package com.example.spreadtrumshitaoli.featurebar;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by SPREADTRUM\shitao.li on 17-6-9.
 */

public class FeatureBar {

    private String Tag = "Featurebar";

    private Button mOptionButton;
    private Button mSelectButton;
    private Button mBackButton;

    private View mDecorView;
    private Context mContext;

    public FeatureBar(Activity activity) {
        mDecorView = activity.getWindow().getDecorView();
        mContext = activity.getApplicationContext();
        wrapDecorView(activity);
    }

    private void wrapDecorView(Activity activity) {
        ViewGroup contenView = (ViewGroup) mDecorView.findViewById(android.R.id.content);
        int childCount = contenView.getChildCount();
        Log.d(Tag,"childCount: "+childCount);
        View[] children = new View[childCount];
        for (int i=0;i<childCount;i++) {
            children[i] = contenView.getChildAt(i);
        }
        contenView.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View featureBar = inflater.inflate(R.layout.decor_layout, null);
        ViewGroup featureBarContent = (ViewGroup) featureBar.findViewById(R.id.content);
        for (int i=0;i<childCount;i++) {
            featureBarContent.addView(children[i]);
        }
        activity.setContentView(featureBar);

        mOptionButton = (Button) featureBar.findViewById(R.id.options);
        mSelectButton = (Button) featureBar.findViewById(R.id.select);
        mBackButton = (Button) featureBar.findViewById(R.id.back);
    }

    public Button getOptionButton() {
        return mOptionButton;
    }
    public Button getSelectButton() {
        return mSelectButton;
    }
    public Button getBackButton() {
        return mBackButton;
    }


}
