package com.example.activitytest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.activitytest.fragment.ArticleFragment;
import com.example.activitytest.fragment.HeadLineFragment;

public class Main2Activity extends AppCompatActivity implements HeadLineFragment.OnHeadlineSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if(findViewById(R.id.frame_layout) != null){
            //如果是恢复之前的状态，就不需要再进行初始化了。
            if(savedInstanceState != null){
                return;
            }

            HeadLineFragment headLineFragment = new HeadLineFragment();

            headLineFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, headLineFragment).commit();
        }
    }

    @Override
    public void onHeadlineSelected(int position) {
        ArticleFragment articleFragment = (ArticleFragment) getSupportFragmentManager().findFragmentById(R.id.article);

        if (articleFragment != null){
            articleFragment.updateArticle(position);
        }else{
            //如果是要替换 Fragment 的话才需要通过传递 Bundle 的方法来传递位置？
            //也就是说，如果当前 Activity 中原本就显示了 Fragment ，就可以直接调用里面的方法，如果没有，就需要通过替换的方式，然后通过 Bundle 传递参数？
            //通过这种方式替换的 ArticleFragment，会执行其所有生命周期的方法，在其 onStart 方法中获取参数。
            //如果是大屏幕的话，首次打开应用时就会创建 ArticleFragment ，而此时 Bunlde 里面没有参数。
            articleFragment = new ArticleFragment();
            Bundle args = new Bundle();
            args.putInt(ArticleFragment.POS_KEY, position);
            articleFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, articleFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

}
