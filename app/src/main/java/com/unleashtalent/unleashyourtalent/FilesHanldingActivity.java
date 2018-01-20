package com.unleashtalent.unleashyourtalent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FilesHanldingActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_hanlding);

        editText=(EditText)(findViewById(R.id.editText2));
        textView=(TextView)(findViewById(R.id.textView2));
    }

    public void Read(View view)
    {
        try {
            FileInputStream fileInputStream= openFileInput("myText.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String lines;
            while ((lines=bufferedReader.readLine())!=null) stringBuffer.append(lines + "\n");
            textView.setText(stringBuffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void Write(View view)
    {
        String Mytextmessage  = editText.getText().toString();
        try {
            FileOutputStream fileOutputStream = openFileOutput("myText.txt",MODE_PRIVATE);
            fileOutputStream.write(Mytextmessage.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(),"Text Saved",Toast.LENGTH_LONG).show();
            editText.setText("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
