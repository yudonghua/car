package com.example.pc.car;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/7/16.
 */
public class DrawView extends View {
    public float currentx = 400;
    public  float currenty = 500 ;
    public float carsize = 25;
    int carnum=4;
    int backwidth=1080,backheight=1920;
    public float x[];
    public  float y[];
    public float x2[];
    public  float y2[];
    public float x3[];
    public  float y3[];
    public float x4[];
    public  float y4[];
    public  float rotate=0;
    public Bitmap car,road;
    /**
     *
     * @param context
     */
    public DrawView(Context context) {
        super(context);
        x= new float[carnum];
        y= new float[carnum];
        x2= new float[carnum];
        y2= new float[carnum];
        x3= new float[carnum];
        y3= new float[carnum];
        x4= new float[carnum];
        y4= new float[carnum];
        car= BitmapFactory.decodeResource(getResources(), R.mipmap.car);
        road=BitmapFactory.decodeResource(getResources(), R.mipmap.road);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //创建画笔 ;

        Paint p = new Paint() ;
        //绘制一个小球 ；
        road(canvas);
        canvas.rotate(rotate,currentx,currenty);
        mycar(canvas);
        canvas.rotate(-rotate,currentx,currenty);
        int width = car.getWidth();
        int height = car.getHeight();
        int newWidth = (int)carsize;
        int newHeight = (int)(carsize*2.2);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(car, 0, 0, width, height, matrix, true);
        for(int i = 0; i<carnum;i++){
            drawcar(canvas,bitmap,(int)x[i],(int)y[i]);
            drawcar(canvas,bitmap,(int)x3[i],(int)y[i]);
        }


    }
    public void drawcar(Canvas canvas,Bitmap bitmap,int x,int y){
        canvas.drawBitmap(bitmap,x,y,null);
    }
    public void mycar(Canvas canvas){
        int width = car.getWidth();
        int height = car.getHeight();
        int newWidth = (int)carsize;
        int newHeight = (int)(carsize*2.2);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(car, 0, 0, width, height, matrix, true);
        canvas.drawBitmap(bitmap,currentx,currenty,null);
    }
    public void road(Canvas canvas){
        int width = road.getWidth();
        int height = road.getHeight();
        int newWidth = (int)(backwidth/3+3*carsize);
        int newHeight = backheight;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(road, 0, 0, width, height, matrix, true);
        canvas.drawBitmap(bitmap,backwidth/3-carsize,0,null);
    }

}

