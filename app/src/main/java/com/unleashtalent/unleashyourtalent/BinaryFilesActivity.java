package com.unleashtalent.unleashyourtalent;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

public class BinaryFilesActivity extends AppCompatActivity {

    TextView binaryTextView;
    EditText binaryEditText;
    int type=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binary_files);

        binaryTextView=(TextView)findViewById(R.id.binaryTextView);
        binaryEditText=(EditText)findViewById(R.id.binaryeditview);

    }

    public void onRadioButtonClicked(View view)
    {
        boolean checked=((RadioButton) view).isChecked();

        //check which radio button was clicked
        switch (view.getId())
        {
            case R.id.charRadioButton:
                if(checked)
                    type=0;
                break;
            case R.id.intRadioButton:
                if(checked)
                    type=1;
                break;
        }


    }
    public  void readBinaryData(View view)
    {
        FileInputStream fis=null;
        try {

            fis = openFileInput("myfile.bin");
            DataInputStream dis=new DataInputStream(fis);

            StringBuilder myString=new StringBuilder();
            int size= dis.read();
            myString.append(size+" ");
            int type=dis.read();
            if(type == 0)
            {
                myString.append("Char"+" ");
                char inputChar;
                for(int i=0;i< size;i++)
                {
                    inputChar=dis.readChar();
                    myString.append(inputChar+" ");
                }
            }
            else if (type == 1)
            {
                myString.append("Integer"+" ");
                int inputInteger;
                for(int i=0;i<size;i++)
                {
                    inputInteger=dis.readInt();
                    myString.append(inputInteger+" ");
                }
            }



            dis.close();


            binaryTextView.setText(myString);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void writeBinaryData(View view)
    {

        try {
            FileOutputStream fos=openFileOutput("myfile.bin", Context.MODE_PRIVATE);
            DataOutputStream dos=new DataOutputStream(fos);
            String inputString="";
            if(type == 0)
            {
                inputString=binaryEditText.getText().toString();
                String[] stringArray=inputString.split(" ");
                char[] charArray=new char[stringArray.length];

                dos.write(charArray.length);
                dos.write(0);

                for (int i = 0; i < stringArray.length; i++) {
                    String stringAsChar = stringArray[i];
                    charArray[i] = stringAsChar.charAt(0);

                    //write the integer value to the file
                    dos.writeChar(charArray[i]);
                }
            }
            else if (type == 1) {
                inputString=binaryEditText.getText().toString();
                String[] stringArray = inputString.split(" ");
                int[] intArray = new int[stringArray.length];

                dos.write(intArray.length);
                dos.write(1);
                for (int i = 0; i < stringArray.length; i++) {
                    String numberAsString = stringArray[i];
                    intArray[i] = Integer.parseInt(numberAsString);

                    //write the integer value to the file
                    dos.writeInt(intArray[i]);
                }

            }


            dos.flush();
            dos.close();


            Toast.makeText(getApplicationContext(),"Values has been saved",Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] stringToBytesUTFCustom(String str) {
        char[] buffer = str.toCharArray();
        byte[] b = new byte[buffer.length << 1];
        for(int i = 0; i < buffer.length; i++) {
            int bpos = i << 1;
            b[bpos] = (byte) ((buffer[i]&0xFF00)>>8);
            b[bpos + 1] = (byte) (buffer[i]&0x00FF);
        }
        return b;

    }

    public  String bytesToStringUTFCustom(byte[] bytes) {
        char[] buffer = new char[bytes.length >> 1];
        for(int i = 0; i < buffer.length; i++) {
            int bpos = i << 1;
            char c = (char)(((bytes[bpos]&0x00FF)<<8) + (bytes[bpos+1]&0x00FF));
            buffer[i] = c;
        }
        return new String(buffer);
    }

    public  byte[] getBytesFromInputStream(InputStream is) throws IOException
    {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();)
        {
            byte[] buffer = new byte[0xFFFF];

            for (int len; (len = is.read(buffer)) != -1;)
                os.write(buffer, 0, len);

            os.flush();

            return os.toByteArray();
        }
    }


}
