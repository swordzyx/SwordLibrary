package com.example.wanandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.wanandroid.adapter.ViewPagerAdapter;
import com.example.wanandroid.fragment.HomeFragment;
import com.example.wanandroid.fragment.NavigateFragment;
import com.example.wanandroid.fragment.ProjectFragment;
import com.example.wanandroid.fragment.UserFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    HomeFragment homeFragment;
    NavigateFragment navigateFragment;
    ProjectFragment projectFragment;
    UserFragment userFragment;
    TabLayout tabLayout ;

    List<Fragment> mLists = new ArrayList<>();
    List<String> title = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViewPager();
    }

    private void initViewPager(){
        viewPager = findViewById(R.id.view_pager);

        if (homeFragment == null){
            homeFragment = new HomeFragment();
            mLists.add(homeFragment);
            title.add("首页");
        }

        if (navigateFragment == null){
            navigateFragment = new NavigateFragment();
            mLists.add(navigateFragment);
            title.add("导航");
        }

        if (projectFragment == null){
            projectFragment = new ProjectFragment();
            mLists.add(projectFragment);
            title.add("项目");
        }

        if (userFragment == null){
            userFragment = new UserFragment();
            mLists.add(userFragment);
            title.add("账户");
        }

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), mLists, title);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tab_id);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        /*switch (item.getItemId()){
            case R.id.search:
            default:
                return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }
}
