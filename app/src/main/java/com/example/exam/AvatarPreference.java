package com.example.exam;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.example.exam.R;

public class AvatarPreference extends Preference {

    public AvatarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.preference_custom_avatar);
    }

    public AvatarPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarPreference(Context context) {
        this(context, null);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        // 可以在这里设置头像图片，例如从 SharedPreferences 加载
    }
}