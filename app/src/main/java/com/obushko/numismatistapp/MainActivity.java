package com.obushko.numismatistapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements ListAdapter.Listener {

    private ArrayList<ListItem> listItems;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private int currentNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        SharedPreferences sharedPreferences = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        currentNightMode = sharedPreferences.getInt("night_mode", Configuration.UI_MODE_NIGHT_NO);

        // Установка режима темы в соответствии с сохраненным значением
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    private void toggleTheme() {
        int newNightMode;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            newNightMode = Configuration.UI_MODE_NIGHT_NO;
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            newNightMode = Configuration.UI_MODE_NIGHT_YES;
        }

        currentNightMode = newNightMode; // Обновляем значение текущего режима

        SharedPreferences sharedPreferences = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("night_mode", currentNightMode);
        editor.apply();

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode != newNightMode) {
            recreate();
            invalidateOptionsMenu();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            toggleTheme();
        }
        return super.onOptionsItemSelected(item);
    }


    private void init() {
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

    @Override
    public void onClickListItem(ListItem listItem, int pos) {

    }




}
