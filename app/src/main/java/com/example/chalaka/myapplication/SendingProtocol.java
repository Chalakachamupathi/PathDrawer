package com.example.chalaka.myapplication;


import java.util.HashMap;

/**
 * Created by chalaka on 4/12/2016.
 */
public class SendingProtocol {

    private int scale = MainActivity.SCALE;
    HashMap < Integer , Point > map;
    private HashMap < Integer , String > toSend = new HashMap<>();
    private int size;

    public SendingProtocol( HashMap< Integer , Point > map ){

        this.map = map;

         size = map.size();
        Point [] tem = new Point[size];
        for (int i = 1 ;i < size ; i++){
            tem[i-1] = map.get(i).getPointDiffrence(map.get(i+1));
        }
        createSendingMap(tem);

    }

    private void createSendingMap(Point[] tem){
        for (int i = 0 ; i < size ; i++){
            if (tem[i] != null)
            if(tem[i].getY() < 0){
                toSend.put(i+1,
                       "+"+ Integer.toString(((int) (tem[i].getX())*scale))
                        );
            }else if (tem[i].getX() < 0){
                toSend.put(i+1,
                        "+"+ Integer.toString(((int) (tem[i].getY())*scale))
                );
            }
        }
    }

    public HashMap< Integer , String > getSendingMap(){
       return this.toSend;
    }


}
