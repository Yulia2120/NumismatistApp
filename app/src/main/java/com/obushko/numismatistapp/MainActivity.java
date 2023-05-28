package com.obushko.numismatistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Document doc;
    private ArrayList<ListItem> listItems;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        Thread secondThread = new Thread(runnable);
        secondThread.start();
    }

    private void getWeb() {
        try {
            Document doc = Jsoup.connect("https://privatbank.ua/premium-banking/coins").get();
            Elements sections = doc.getElementsByClass("wr_inner block-wtf coins-container");

            if (sections.size() > 0) {
                Element section = sections.first();
                Elements rows = section.getElementsByClass("item-table");

                for (Element row : rows) {
                    Element imgElement = row.select("img").first();
                    if (imgElement != null) {
                        String src = imgElement.attr("data-src");

                        String title = row.select(".table-description.bold-text").first().text();
                        String price = row.select(".table-description.coin-name").first().text();

                        ListItem listItem = new ListItem();
                        listItem.setUrlImage(src);
                        listItem.setTitle(title);
                        listItem.setPrice(price);
                        listItems.add(listItem);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listAdapter = new ListAdapter(MainActivity.this, listItems);
                        recyclerView.setAdapter(listAdapter);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}