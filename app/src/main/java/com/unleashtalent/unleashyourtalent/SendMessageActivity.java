package com.unleashtalent.unleashyourtalent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SendMessageActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE="com.unleashtalent.Message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
    }

    public void SendMessage(View view)
    {
        Intent intent=new Intent();
        EditText editText=(EditText)findViewById(R.id.editText);
        String message=editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE,message);
        setResult(RESULT_OK,intent);
        finish();
    }
}
