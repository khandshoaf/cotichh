package run;

import bot.BaseBot;
import bot.CategoryCrawler;
import bot.StoryCrawler;
import com.google.common.collect.BoundType;
import database.Jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class main {
    public static void main(String[] args) {
        String baseUrl = "https://cotich.net/";

        CategoryCrawler categoryCrawler = new CategoryCrawler(baseUrl);
        StoryCrawler storyCrawler = new StoryCrawler(baseUrl);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(categoryCrawler.isRunning){
                    categoryCrawler.startCrawling();
                }else{
                    categoryCrawler.isRunning = false;
                    System.out.println("---------------------------bot category = false--------------------------");
                }

                if(storyCrawler.isRunning){
                    storyCrawler.startCrawling();
                }else{
                    storyCrawler.isRunning = false;
                    System.out.println("---------------------------bot story = false--------------------------");
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,0,1000);
    }
}
