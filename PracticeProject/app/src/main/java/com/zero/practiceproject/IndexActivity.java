  
package com.zero.practiceproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zero.practiceproject.service.ServiceActivity;

public class IndexActivity extends Activity {
    @Override
    public void onCreate(Bundle saveInstance){
        super.onCreate(saveInstance);
        setContentView(R.layout.activity_index);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.service_activity:
                Intent intent = new Intent(IndexActivity.this, ServiceActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}