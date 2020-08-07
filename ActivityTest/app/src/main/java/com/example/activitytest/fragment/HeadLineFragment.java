package com.example.activitytest.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.activitytest.R;
import com.example.activitytest.util.Ipsum;

public class HeadLineFragment extends ListFragment {
    OnHeadlineSelectedListener onHeadlineSelectedListener;

    @Override
    public void onCreate(Bundle saveInstanceState){
       super.onCreate(saveInstanceState);

       int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
               android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

       //onCreate 中初始化数据
       setListAdapter(new ArrayAdapter<String>(getContext(), layout, Ipsum.Headlines));
   }

   @Override
   public void onStart(){
        super.onStart();

        if (getFragmentManager().findFragmentById(R.id.article) != null){
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
   }

   @Override
   public void onAttach(Activity activity) {
       super.onAttach(activity);

       try{
           onHeadlineSelectedListener = (OnHeadlineSelectedListener)activity;
       }catch(ClassCastException Exception){
           throw new ClassCastException(activity.toString() +
                   " must implement OnHeadlineSelectedListener");
       }

   }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        onHeadlineSelectedListener.onHeadlineSelected(position);

        getListView().setItemChecked(position, true);
    }

    public interface OnHeadlineSelectedListener{
        void onHeadlineSelected(int position);
    }
}
