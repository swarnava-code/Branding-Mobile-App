package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//AppCompatActivity
public class BlogActivity extends AppCompatActivity  {

    WebView webView;
    String url = "https://vaishnavibestdeal.com/";  //"https://adosy.in/category/blogs/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        /*
        webView = findViewById(R.id.webView);
        webView.loadUrl(url);
         */


/*
        WebView myView = findViewById(R.id.webView);
        myView.setWebViewClient(new WebViewClient());
        myView.getSettings().setJavaScriptEnabled(true);
        myView.loadUrl("https://www.stackoverflow.com");

 */

/*
        webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView .getSettings().setJavaScriptEnabled(true);
        webView .getSettings().setDomStorageEnabled(true);
        webView.loadUrl("https://www.stackoverflow.com");

 */



    }
}