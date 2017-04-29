package com.example.pc.car;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Runnable{

    private Thread newThread;
    static boolean touch=false;
    static boolean live=true;
    static float v,upv=0,hv=0,downv=0,last=0;
    static float carsize=0,wallsize=0,width=1980,height=1080;
    static int x=100,y=500,k=0,i=0,num=1,best=0,score=0;
    static float t1=0,t2=0,datx=0,daty=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LinearLayout main = (LinearLayout)findViewById(R.id.root) ;
        final DrawView draw = new DrawView(this) ;
        SharedPreferences best0 = getSharedPreferences("best",
                Activity.MODE_PRIVATE);
        int history = best0.getInt("best", 0);
        TextView besttext = (TextView)findViewById(R.id.best);
        besttext.setText("最高分："+history);
        best=history;
        draw.setMinimumWidth(300) ;
        draw.setMinimumHeight(500) ;
        final RelativeLayout re = (RelativeLayout) findViewById(R.id.re) ;
        main.post(new Runnable() {
            @Override
            public void run() {
                width=re.getWidth();
                height=re.getHeight();
                v=height/900;
                upv=(float)(v*Math.cos(draw.rotate/180*Math.PI));
                hv=(float)(v*Math.sin(draw.rotate/180*Math.PI));
                carsize=height/60;
                draw.carsize=carsize;
                draw.backwidth=(int)width;
                draw.backheight=(int)height;
                draw.currentx=width/2;
                draw.currenty=height/5*3;
                draw.y[0]=height-carsize*3;
                for(int i = 1; i<draw.carnum;i++){
                    draw.y[i]=draw.y[i-1]-height/draw.carnum;
                    draw.y2[i]=draw.y2[i-1]-height/draw.carnum;
                }
                for(int i = 0; i<draw.carnum;i++){
                    draw.x[i]=width/3+(float)Math.random()*(width/6-carsize);
                    draw.x2[i]=draw.x[i]+width/3;
                    draw.x3[i]=width/2+(float)Math.random()*(width/6);
                    draw.x4[i]=draw.x3[i]+width/3;
                }
            }
        });



        draw.setOnTouchListener(new View.OnTouchListener()
        {


            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                if(Math.abs(event.getX()-last)>100){
                    last=event.getX();
                    return true;
                }
                draw.rotate+=0.12*(event.getX()-last)*1080/width;
                if(draw.rotate>60)draw.rotate=60;
                if(draw.rotate<-60)draw.rotate=-60;
                last=event.getX();
                touch = true;
                return true;

            }

        }) ;
        main.addView(draw) ;


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(live){
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    if(draw.rotate>0)draw.rotate-=0.2;
//                    if(draw.rotate<0)draw.rotate+=0.2;
                    turn(draw.rotate);
                    for(int i = 0; i<draw.carnum;i++){
                        if(draw.y[i]<height+carsize*2.2){
                            if(draw.currenty-draw.y[i]>0&&draw.currenty-draw.y[i]<height/draw.carnum&&draw.currentx<width/2){
                                if(draw.x[i]>draw.currentx){
                                    draw.x[i]-=v/20;
                                }
                                else if(draw.x[i]<width/2-carsize)draw.x[i]+=v/20;
                            }
                            if(draw.currenty-draw.y[i]>0&&draw.currenty-draw.y[i]<height/draw.carnum&&draw.currentx>=width/2){
                                if(draw.x3[i]>draw.currentx){
                                    draw.x3[i]-=v/20;
                                }
                                else draw.x3[i]+=v/20;
                            }
                            if(touch){
                                draw.y[i]+=upv;
                                draw.y[i]-=v/2;
                            }

                        }
                        else {
                            draw.y[i]=0;
                            score++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView currentscore = (TextView)findViewById(R.id.score);
                                    currentscore.setText("得分："+score);
                                }
                            });
                        }
                    }


                    draw.currentx+=hv;
                    if(draw.currentx<width/3){
                        draw.rotate=0;
                        draw.currentx=width/3;
                    }
                    if(draw.currentx>width*2/3){
                        draw.rotate=0;
                        draw.currentx=width*2/3;
                    }
                    draw.postInvalidate();
                    for (int i = 0;i<draw.carnum;i++){
                        if(draw.currentx>=draw.x[i]-carsize&&draw.currentx<=draw.x[i]+carsize)
                            if(draw.currenty>=draw.y[i]-(carsize*2.2)&&draw.currenty<=draw.y[i]+(carsize*2.2)){
                                draw.rotate=0;
                                touch=false;
                                live=false;
                                draw.postInvalidate();
                                main.post(MainActivity.this);
                                break;
                            }
                    }
                    for (int i = 0;i<draw.carnum;i++){
                        if(draw.currentx>=draw.x3[i]-carsize&&draw.currentx<=draw.x3[i]+carsize)
                            if(draw.currenty>=draw.y[i]-(carsize*2.2)&&draw.currenty<=draw.y[i]+(carsize*2.2)){
                                draw.rotate=0;
                                touch=false;
                                live=false;
                                draw.postInvalidate();
                                main.post(MainActivity.this);
                                break;
                            }
                    }



                }
            }
        }).start();


    }








    public void turn(float t){
        upv=Math.abs((float)(v*Math.cos(t/180*Math.PI)));
        hv=(float)(v*Math.sin(t/180*Math.PI));
    }

    @Override
    public void run() {
        if(best<score)best=score;
        SharedPreferences best0 = getSharedPreferences("best", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = best0.edit();
        editor.putInt("best", best);
        editor.commit();
        TextView bestscore = (TextView)findViewById(R.id.best2);
        bestscore.setVisibility(View.VISIBLE);
        bestscore.setText("最高分："+best);
        TextView currentscore = (TextView)findViewById(R.id.score2);
        currentscore.setVisibility(View.VISIBLE);
        currentscore.setText("得分："+score);
        Button button=(Button)findViewById(R.id.button);
        button.setVisibility(View.VISIBLE);
    }
    public void replay(View view){
        score=0;
        touch=false;
        live=true;
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


}
