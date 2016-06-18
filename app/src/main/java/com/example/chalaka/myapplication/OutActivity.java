package com.example.chalaka.myapplication;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class OutActivity extends AppCompatActivity {
    private ListView mList;
    private ArrayList<String> arrayList;
    private ArrayList<String> inputMessages;
    private MyCustomAdapter mAdapter;
    private TCPClient mTcpClient;
    private HashMap< Integer , Point > hashMap;
    private HashMap< Integer , String > toSendValues;
    private static int count = 1;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView)findViewById(R.id.texConStatus);
        arrayList = new ArrayList<>();
        inputMessages = new ArrayList<>();

        final EditText editText = (EditText) findViewById(R.id.editText);
        Button send = (Button)findViewById(R.id.send_button);

        //relate the listView from java to the one created in xml
        mList = (ListView)findViewById(R.id.list);
        mAdapter = new MyCustomAdapter(this, arrayList);
        mList.setAdapter(mAdapter);
        //getting extraa
        hashMap = (HashMap<Integer , Point>) this.getIntent().getSerializableExtra("hashMap");

        toSendValues = new SendingProtocol(hashMap).getSendingMap();

        Log.d("TCP", "before start");
        // connect to the server
        new connectTask().execute("");

        Log.d("TCP", "after start");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString();

                //add the text in the arrayList
                arrayList.add("c: " + message);

                //sends the message to the server
                if (mTcpClient != null) {
                    //send the first value
                    mTcpClient.sendMessage(toSendValues.get(count));
                    Log.d("TCP", "output massage :" + message);
                }

                //refresh the list
                mAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public class connectTask extends AsyncTask<String,String,TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(textView , new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    Log.d("TCP", "input massage"+message);
                    if(message.equalsIgnoreCase("DONE")){
                        if(!toSendValues.isEmpty()) {
                            mTcpClient.sendMessage(toSendValues.get(count));
                            count++;
                        }
                    }
                    inputMessages.add(message);
                    publishProgress(message);

                }
            });

                mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            //in the arrayList we add the messaged received from server
            arrayList.add(values[0]);
            Log.d("TCP", "inprograss : " + values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list

            mAdapter.notifyDataSetChanged();
        }
    }

}
