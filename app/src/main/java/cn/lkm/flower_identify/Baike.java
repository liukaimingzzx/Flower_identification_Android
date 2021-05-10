package cn.lkm.flower_identify;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Baike extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baike);
        Intent intent = getIntent();
        String data = intent.getStringExtra("url");
        System.out.println(data);
        WebView webView = (WebView)findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(data);
    }
}
