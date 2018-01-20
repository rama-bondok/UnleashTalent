package com.unleashtalent.unleashyourtalent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static final int MESSAGE_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        // Make sure the request was successful
        if (requestCode == MESSAGE_REQUEST) if (resultCode == RESULT_OK) {

            // Get the Intent that started this activity and extract the string
            String message = data.getStringExtra(SendMessageActivity.EXTRA_MESSAGE);

            // Capture the layout's TextView and set the string as its text
            TextView textView = findViewById(R.id.textView);
            textView.setText(message);
        }
    }

    public void StartAnotherActivity(View view)
    {
        Intent intent=new Intent(this, SendMessageActivity.class);
        startActivityForResult(intent,MESSAGE_REQUEST);
    }

    public void StartFilesActivity(View view)
    {
        Intent intent=new Intent(this,FilesHanldingActivity.class);
        startActivity(intent);
    }

}
