package fractal.visaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class ViewFile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file);

        Intent i =getIntent();

        String empcode=i.getStringExtra("empcode");

        WebView webView = (WebView) findViewById(R.id.webView);
        String url = "http://192.168.0.104:8080/web/viewer.html?file=uploads/"+empcode+".pdf";
        //String url = "http://drive.google.com/viewerng/viewer?embedded=true&url=" + myPdfUrl;
        //Log.i(TAG, "Opening PDF: " + url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}

/*


public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);




    }
}

 */