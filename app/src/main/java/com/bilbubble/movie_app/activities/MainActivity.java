package com.bilbubble.movie_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.bilbubble.movie_app.network.Const;
import com.bilbubble.movie_app.utils.ActionBarTitle;
import com.bilbubble.movie_app.R;
import com.bilbubble.movie_app.fragments.FavoriteFragment;
import com.bilbubble.movie_app.fragments.MovieFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ActionBarTitle {

    private String layoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu_item_tv_now_playing);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.menu_item_tv_now_playing:
                layoutName = Const.NOW_PLAYING;
                setActionBarTitle(layoutName);
                fragment = new MovieFragment(layoutName);
                break;
            case R.id.menu_item_tv_upcoming:
                layoutName = Const.UPCOMING;
                setActionBarTitle(layoutName);
                fragment = new MovieFragment(layoutName);
                break;
            case R.id.menu_item_tv_popular:
                layoutName = Const.POPULAR;
                setActionBarTitle(layoutName);
                fragment = new MovieFragment(layoutName);
                break;
            case R.id.menu_item_tv_favorites:
                layoutName = Const.FAVORITES;
                setActionBarTitle(layoutName);
                fragment = new FavoriteFragment();
                break;
        }

        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public void setActionBarTitle(String title) {
        View view = getLayoutInflater().inflate(R.layout.action_bar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
        );

        TextView titleBar = view.findViewById(R.id.tv_ab_title);
        titleBar.setText(title);

        getSupportActionBar().setCustomView(view, params);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}