package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;


public class BlogActivity extends AppCompatActivity {

    WebView webView;

    String url = "https://adosy.in/category/blogs/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        webView = findViewById(R.id.webview);


        webView.loadUrl(url);


    }
}