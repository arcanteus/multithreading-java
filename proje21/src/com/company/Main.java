package com.company;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Random;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class Main extends  JFrame {
    static Random randomnum=new Random();
    static mainThread mT=new mainThread();
    static eraserThread eT=new eraserThread();
    static writeThread wT=new writeThread();
    static ArrayList<subThread> subTs=new ArrayList<>();
    static JFrame f;
    static JPanel p;
    static JProgressBar mainbar;

    public static void main(String[] args) {
        f = new JFrame("ProgressBar demo");
        p = new JPanel();


        mainbar = new JProgressBar();
        mainbar.setValue(1);
        mainbar.setStringPainted(true);
        p.add(mainbar);
        f.add(p);

        mT.run();
         subThread sb1=new subThread();
         subThread sb2=new subThread();
        f.setSize(500, 500);
        f.setVisible(true);
         sb1.ekle();
         sb2.ekle();
         sb1.run();
         sb2.run();

         subTs.add(sb1);
         subTs.add(sb2);
         eT.run();
         wT.run();
    }





}

class writeThread extends  Main{

    public Timer tmer = new Timer();
    public TimerTask task1 = new TimerTask() {

        @Override
        public void run() {

            double percent= (mainThread.mT.istek/(double)mainThread.mT.kapasite)*100;
            mainbar.setValue((int)percent);
            //System.out.println("main thread: "+ " yuzde="+percent+" istek="+mT.istek+" kapasite="+mT.kapasite);
            for (int i = 0; i <subTs.size() ; i++) {
                percent = (subTs.get(i).istek/(double)subTs.get(i).kapasite)*100;
                subTs.get(i).bar.setValue((int)percent);
                //System.out.println("sub thread "+i+ ": yuzde="+percent+" istek="+subTs.get(i).istek+" kapasite="+subTs.get(i).kapasite);
            }
            System.out.println("\n\n\n");
        }
    };

    public void run(){

        tmer.schedule(task1, 0, 100);

    }
}

class eraserThread extends  Main{

    public Timer tmer = new Timer();
    public TimerTask task1 = new TimerTask() {

        @Override
        public void run() {

            //double percent= (mainThread.mT.istek/(double)mainThread.mT.kapasite)*100;

            for (int i = 0; i <subTs.size() ; i++) {
                double percent = (subTs.get(i).istek/(double)subTs.get(i).kapasite)*100;
                if (percent >70){
                    subThread sub=new subThread();
                    sub.run();
                    sub.ekle();
                    subTs.add(sub);


                }if (subTs.get(i).istek<=0){
                    if(subTs.size()>2){
                    subTs.get(i).tmer.cancel();
                    p.remove(subTs.get(i).bar);
                    f.invalidate();
                    f.validate();
                    f.repaint();
                    subTs.remove(i);}
                }
            }

        }
    };

    public void run(){

        tmer.schedule(task1, 0, 50);

    }



}

class subThread extends  Main{

    public int istek=0;
    public int kapasite=5000;
    public JProgressBar bar;
    public Timer tmer = new Timer();
    public TimerTask task1 = new TimerTask() {

        @Override
        public void run() {
            int request=randomnum.nextInt()%100+1;
            if(Main.mT.istek-request<0){
                request=Main.mT.istek;
                Main.mT.istek=0;
            }else{
                Main.mT.istek-=request;
            }
            if(istek+request>kapasite){
                istek=kapasite;
            }else {

                istek+=request;
            }

        }
    };
    public TimerTask task2 = new TimerTask() {

        @Override
        public void run() {
            int request=randomnum.nextInt()%50+1;
            if(istek-request<0){
                istek=0;
            }else {

                istek-=request;
            }

        }
    };
    public void run(){

        tmer.schedule(task1, 0, 50);
        tmer.schedule(task2, 0, 30);
    }


    public void ekle() {
        bar = new JProgressBar();
        bar.setValue(1);
        bar.setStringPainted(true);
        p.add(bar);
        f.add(p);
        f.invalidate();
        f.validate();
        f.repaint();
    }
}
class mainThread extends Main {

    public int istek=0;
    public int kapasite=10000;
    public Timer tmer = new Timer();
    public TimerTask task1 = new TimerTask() {

        @Override
        public void run() {
            int request=randomnum.nextInt()%500+1;
            if(istek+request>kapasite){
                istek=kapasite;
            }else {

                istek+=request;
            }

        }
    };
    public TimerTask task2 = new TimerTask() {

        @Override
        public void run() {
            int request=randomnum.nextInt()%100+1;
            if(istek-request<0){
                istek=0;
            }else {

                istek-=request;
            }

        }
    };
    public void run(){

        tmer.schedule(task1, 0, 50);
        tmer.schedule(task2, 0, 30);
    }


}