package com.example.spreadtrumshitaoli.demo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends Activity {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_SECONDS = 30;

    private static final int SLEEP_TIME = 20;
    private static final int BUTTON_NORMAL = 0x0;
    private static final int BUTTON_EXIT_ALIVE_SECONDS = 0x1;
    private static final int BUTTON_EXIT_MAXPOLL_SIZE = 0X2;


    private String TAG = "TaskDemo";
    private int mFlag = BUTTON_NORMAL ;
    private int sleepTime = SLEEP_TIME;

    private ProgressBar mProgressBar;
    MyAsyncTask myAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String TaskParams = "CPU count: "+String.valueOf(CPU_COUNT)+"\n"
                +"Core poll size : "+String.valueOf(CORE_POOL_SIZE)+"\n"
                +"Max poll size: "+String.valueOf(MAXIMUM_POOL_SIZE)+"\n"
                +"Alive seconds: "+String.valueOf(KEEP_ALIVE_SECONDS);
        TextView textView = (TextView) findViewById(R.id.textView8);
        textView.setText(TaskParams);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        myAt = new MyAsyncTask((TextView)findViewById(R.id.tv));

        //start task
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Noarmal Start Task...");
                mFlag |= BUTTON_NORMAL;
                startTask();
                findViewById(R.id.start).setEnabled(false);
            }
        });
        findViewById(R.id.es).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Exit Alive Second start...");
                mFlag |= BUTTON_EXIT_ALIVE_SECONDS;
                startTask();
                findViewById(R.id.es).setEnabled(false);
            }
        });
        findViewById(R.id.ems).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Exit Max Poll Size start...");
                mFlag |= BUTTON_EXIT_MAXPOLL_SIZE;
                startTask();
                findViewById(R.id.ems).setEnabled(false);
            }
        });
        //cancel task
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Cancel task.");
                myAt.cancel(true);
            }
        });
    }


    private void startTask(){
        switch (mFlag) {
            case BUTTON_NORMAL:
                doTask();
                mFlag &= 0x0;
                break;
            case BUTTON_EXIT_ALIVE_SECONDS:
                sleepTime = KEEP_ALIVE_SECONDS*10;
                doSerialTask(MAXIMUM_POOL_SIZE+CORE_POOL_SIZE);
                mFlag &= 0x0;
                break;
            case BUTTON_EXIT_MAXPOLL_SIZE:
                sleepTime = SLEEP_TIME;
                doSerialTask(BUTTON_EXIT_MAXPOLL_SIZE+MAXIMUM_POOL_SIZE+CORE_POOL_SIZE+1000);
                mFlag &= 0x0;
                break;
        }
    }

    private void doTask() {
        if (false) {
            myAt.execute("start");
            MyAsyncTask asyncTask1 = new MyAsyncTask((TextView) findViewById(R.id.textView2));
            asyncTask1.execute("1");
            MyAsyncTask asyncTask2 = new MyAsyncTask((TextView) findViewById(R.id.textView3));
            asyncTask2.execute("2");
            MyAsyncTask asyncTask3 = new MyAsyncTask((TextView) findViewById(R.id.textView4));
            asyncTask3.execute("3");
            MyAsyncTask asyncTask4 = new MyAsyncTask((TextView) findViewById(R.id.textView5));
            asyncTask4.execute("4");
            MyAsyncTask asyncTask5 = new MyAsyncTask((TextView) findViewById(R.id.textView6));
            asyncTask5.execute("5");
            MyAsyncTask asyncTask6 = new MyAsyncTask((TextView) findViewById(R.id.textView7));
            asyncTask6.execute("6");
        } else {
            myAt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "satrt");
            MyAsyncTask asyncTask1 = new MyAsyncTask((TextView) findViewById(R.id.textView2));
            asyncTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
            MyAsyncTask asyncTask2 = new MyAsyncTask((TextView) findViewById(R.id.textView3));
            asyncTask2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "2");
            MyAsyncTask asyncTask3 = new MyAsyncTask((TextView) findViewById(R.id.textView4));
            asyncTask3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "3");
            MyAsyncTask asyncTask4 = new MyAsyncTask((TextView) findViewById(R.id.textView5));
            asyncTask4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "4");
            MyAsyncTask asyncTask5 = new MyAsyncTask((TextView) findViewById(R.id.textView6));
            asyncTask5.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "5");
            MyAsyncTask asyncTask6 = new MyAsyncTask((TextView) findViewById(R.id.textView7));
            asyncTask6.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "6");
        }
    }

    private void doSerialTask(int count) {
        for (int i=0; i<count; i++) {
            new MyAsyncTask().execute(String.valueOf(i));
        }
    }

   /*
    * Class MyAsyncTask
    * */
    public class MyAsyncTask extends AsyncTask<String, Integer, String> {

        private TextView mTextView;

        public MyAsyncTask(TextView textView) {
            mTextView = textView;
        }
        public MyAsyncTask(){
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG,"doInBackground.");
            doCostFunction(params);
            return params[0];
        }

        @Override
        protected void onPostExecute(String string) {
            Log.d(TAG,"onPostExecute.");
            super.onPostExecute(string);
            if (mTextView!=null) {
                mTextView.setText("onPostExecute: "+string);
            } else {
                Log.d(TAG,"onPostExecute : "+string);
            }
        }

        private void doCostFunction(String... params) {
            int i;
            for (i=0;i<100;i++) {
                if (isCancelled()) {
                    break;
                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                publishProgress(i);
            }
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG,"onPreExecute.");
            super.onPreExecute();
            if (mTextView!=null) {
                mTextView.setText("Loading... ... ...");
            } else {
                Log.d(TAG,"onPreExecute ... ... ...");
            }
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG,"onCancelled.");
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //Log.d(TAG,"onProgressUpdate.");
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
        }
    }


}
