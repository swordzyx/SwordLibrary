package com.example.activitytest.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.activitytest.R;
import com.example.activitytest.fragment.DetailsFragment;
import com.example.activitytest.util.Shakespeare;

public class FragmentTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
    }

    public class TitleFragments extends ListFragment{
        boolean dualPane;
        int curPosition;
        @Override
        public void onActivityCreated(Bundle saveInstanceState){
            super.onActivityCreated(saveInstanceState);

            setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, Shakespeare.TITLES));

            View detailsFrame = getActivity().findViewById(R.id.details);

            dualPane = detailsFrame != null || detailsFrame.getVisibility() == View.VISIBLE;

            if (saveInstanceState != null){
                curPosition = saveInstanceState.getInt("curPosition", 0);
            }

            if (dualPane){
                getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                showDetails(curPosition);
            }
        }

        public void showDetails(int position){
            if (dualPane){
                getListView().setItemChecked(position, true);

                DetailsFragment details = (DetailsFragment)getSupportFragmentManager().findFragmentById(R.id.details);
                if (details == null || details.getShowIndex() != position){
                    DetailsFragment f = DetailsFragment.getInstance(position);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    if (position != 0){
                        transaction.replace(R.id.details, f);
                    }
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();
                }

            }else{
                Intent intent = new Intent(FragmentTestActivity.this, DetailActivity.class);
                intent.putExtra("position", position);
                getContext().startActivity(intent);
            }
        }

    }
}
