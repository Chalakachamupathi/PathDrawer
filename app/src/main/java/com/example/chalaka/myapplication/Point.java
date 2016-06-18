package com.example.chalaka.myapplication;

import java.io.Serializable;

/**
 *
 * Created by chalaka on 4/14/2016.
 */

public class Point implements Serializable{

    private double x;
    private double y;

    public Point(double x , double y ){
        this.x = x;
        this.y = y;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public Point getPointDiffrence(Point p){
        return new Point((this.x - p.getX()) , (this.y - p.getY()));
    }
}
