package com.example.dell.jiemian;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;

public class questionActivity extends Activity {
    private String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_question);
        ActivityClearer.removeActivity("cryActivity");
        ActivityClearer.addActivity("questionActivity",this);

        TextView question1 = (TextView)findViewById(R.id.question1);
        TextView question2 = (TextView)findViewById(R.id.question2);
        Random random = new Random();
        int number = random.nextInt(4);
        if(number == 1){
            question1.setText("婴儿是否有乱动的情况？");
        }
        else if(number == 2){
            question2.setText("婴儿有没有脸红，流鼻涕的情况？");
        }

        Intent intent =getIntent();
        data = intent.getStringExtra("extra_data");

        Button RG = (Button)findViewById(R.id.button);
        RG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(questionActivity.this,resultActivity.class);
                intent.putExtra("extra_data",data);
                startActivity(intent);
            }
        });
    }
    @Override
    public void finish(){
        super.finish();
        onDestroy();
    }
}
