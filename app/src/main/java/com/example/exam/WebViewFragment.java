package com.example.exam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {

    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance() {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
        WebView webView = rootView.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true); // 启用JavaScript支持
        webView.setWebViewClient(new WebViewClient()); // 设置WebView客户端
        webView.loadUrl("http://news.sina.com.cn/");
        return rootView;
    }
}