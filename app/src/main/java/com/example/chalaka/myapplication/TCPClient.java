package com.example.chalaka.myapplication;

/**
 *
 * Created by chalaka on 4/3/2016.
 */
import android.util.Log;
import android.widget.TextView;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;


public class TCPClient {

    private String serverMessage;
    /*public static final String SERVERIP = "192.168.137.1"; //your computer IP address
    public static final int SERVERPORT = 4444;*/
    private String SERVERIP = MainActivity.SERVERIP;
    private int SERVERPORT  = MainActivity.SERVERPORT;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private TextView textView;
    PrintWriter out;
    BufferedReader in;

    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(TextView textView ,OnMessageReceived listener) {
        mMessageListener = listener;
        this.textView = textView;
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }



    public void stopClient(){
        mRun = false;
    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);

            try {

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Log.d("TCP Client", "C: Sent.");

                Log.d("TCP Client", "C: Done.");
                textView.setText("connected");
                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                try {
                    //in this while the client listens for the messages sent by the server

                    while (mRun) {
                        serverMessage = in.readLine();

                        if (serverMessage != null && mMessageListener != null) {
                            //call the method messageReceived from MyActivity class
                            mMessageListener.messageReceived(serverMessage);

                            Log.d("TCP", "S: Received Message: '" + serverMessage + "'");
                        }
                        serverMessage = null;

                    }
                }catch(SocketException e){
                    textView.setText("connection lost");
                }
                Log.d("TCP", "S: Received Message: '" + serverMessage + "'");
                Log.d("TCP Client", serverMessage);
            } catch (Exception e) {
                textView.setText("unable to connect");
                Log.d("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {

            Log.d("TCP", "C: Error", e);

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
