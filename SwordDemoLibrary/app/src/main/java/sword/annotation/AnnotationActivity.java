package sword.annotation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.swordlibrary.R;
import com.sword.annotation.BindView;
import com.sword.processor.Binding;

@SuppressLint("NonConstantResourceId")
public class AnnotationActivity extends Activity {
    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.layout)
    ViewGroup layout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);

        Binding.bind(this);
        textView.setText("HenCoder");
        layout.setBackgroundColor(Color.CYAN);
    }
}
