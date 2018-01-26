package com.unleashtalent.unleashyourtalent;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XMLFilesActivity extends AppCompatActivity {

    TextView xmlTextView;
    Button readxmlButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmlfiles);

        //disable the read xml file read button
        readxmlButton=(Button)findViewById(R.id.readxmlbutton);
        readxmlButton.setEnabled(false);

        // get a reference to textview to display xml file content
        xmlTextView=(TextView)findViewById(R.id.xmltextveiw);
    }

    public void readXmlFile(View view)
    {

        XmlPullParserFactory factory;
        FileInputStream fis=null;
        try
        {
            StringBuilder sb=new StringBuilder();
            factory=XmlPullParserFactory.newInstance();
            XmlPullParser xpp=factory.newPullParser();

            fis=openFileInput("people.xml");

            xpp.setInput(fis,null);

            int eventType=xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                if(eventType == XmlPullParser.START_DOCUMENT)
                    sb.append("[START]");
                else if(eventType == XmlPullParser.START_TAG)
                    sb.append("\n<"+ xpp.getName()+">");
                else if(eventType == XmlPullParser.END_TAG)
                    sb.append("\n</"+ xpp.getName()+">");
                else if(eventType == XmlPullParser.TEXT)
                    sb.append(xpp.getText());

                eventType=xpp.next();
            }

            xmlTextView.setText(sb.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void writeXmlFile(View view)
    {

        //disable xml file write button
        Button writexmlButton=(Button)view;
        writexmlButton.setEnabled(false);

        //enable xml file read button
        readxmlButton.setEnabled(true);

        //get the initial xml file content
        String xmlFileContent=initializeXmlFileContent();

        try
        {
            FileOutputStream fos=openFileOutput("people.xml", Context.MODE_PRIVATE);
            fos.write(xmlFileContent.getBytes());
            fos.close();

            xmlTextView.setText(xmlFileContent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }


    private String initializeXmlFileContent()
    {

        ArrayList<String> people=new ArrayList<>();
        people.add("Fadi");
        people.add("Rama");
        people.add("Karim");
        people.add("Sara");

        String result="";

        //initialize xml serializer
        XmlSerializer serializer= Xml.newSerializer();
        StringWriter writer=new StringWriter();

        try
        {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8",true);
            serializer.startTag("","people");
            serializer.attribute("","number",String.valueOf(people.size()));

            for(String person: people)
            {
                serializer.startTag("","person");
                serializer.text(person);
                serializer.endTag("","person");
            }

            serializer.endTag("","people");
            serializer.endDocument();
            result=writer.toString();

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally {
            return  result;
        }
    }
}
