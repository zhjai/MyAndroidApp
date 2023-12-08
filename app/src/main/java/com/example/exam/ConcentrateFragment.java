package com.example.exam;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.exam.data.TimerViewModel;
import com.example.exam.view.TimerView;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConcentrateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConcentrateFragment extends Fragment {

    private TimerViewModel timerViewModel;
    private TextView inputFocusEvent;
    private TimerView timerView;
    private TextView timerTextView;
    MaterialButton startFocusButton;
    private Long totalElapsedTime = 25 * 60 * 1000L;

    public ConcentrateFragment() {
        // Required empty public constructor
    }

    public static ConcentrateFragment newInstance() {
        ConcentrateFragment fragment = new ConcentrateFragment();
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

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_concentrate, container, false);

        inputFocusEvent = rootView.findViewById(R.id.input_event_text_view);
        timerView = rootView.findViewById(R.id.timer_view);
        timerTextView = rootView.findViewById(R.id.timer_text_view);
        startFocusButton = rootView.findViewById(R.id.start_concentrate_button);
        LinearLayout focusingLayout = rootView.findViewById(R.id.focusing_layout);

        MainActivity2 activity = (MainActivity2) getActivity();
        if (activity != null) {
            timerViewModel = activity.getTimerViewModel();
        }

        timerViewModel.getElapsedTime().observe(getViewLifecycleOwner(), millisUntilFinished -> {
            float progress = millisUntilFinished / (float) timerViewModel.getTotalTime();
            timerView.setProgress(progress);
            timerTextView.setText(formatTime(millisUntilFinished));
            if (timerViewModel.isTimerRunning()) {
                focusingLayout.setVisibility(View.VISIBLE);
                timerTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.deep_green));
                startFocusButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
                startFocusButton.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_stop));
            }
            else {
                focusingLayout.setVisibility(View.INVISIBLE);
                timerTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                startFocusButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.deep_green));
                startFocusButton.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_start));
                timerView.setProgress(1);
                timerTextView.setText(formatTime(totalElapsedTime));
            }
        });
        timerViewModel.getElapsedTime().setValue(totalElapsedTime);

        inputFocusEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("专注事件");

                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_input_focus_event, null);
                EditText editInputFocusEvent = dialogView.findViewById(R.id.edit_focus_event);
                builder.setView(dialogView);

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputFocusEvent.setText(editInputFocusEvent.getText().toString());
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                Dialog dialog = builder.create();
                dialog.show();
            }
        });

        timerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFocusTimePickerDialog();
            }
        });

        startFocusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerViewModel.isTimerRunning()) {
                    // 开始计时
                    timerViewModel.startTimer(totalElapsedTime);
                    // 更改按钮的背景颜色、文字和图标
                    startFocusButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
                    startFocusButton.setText("放弃专注");
                    // 这里设置图标的代码取决于你的图标是什么以及如何设置
                    startFocusButton.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_stop));
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("放弃专注");
                    builder.setMessage("确定要放弃当前的专注吗？");

                    // 设置PositiveButton
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 用户确认放弃专注
                            // 在这里执行放弃专注的逻辑，比如停止计时器等
                            timerViewModel.stopTimer();
                            // 恢复按钮的原始状态
                            startFocusButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.deep_green));
                            startFocusButton.setText("开始专注");
                            timerViewModel.getElapsedTime().setValue(totalElapsedTime);
                            // 设置原始图标
                            startFocusButton.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_start));
                        }
                    });

                    // 设置NegativeButton
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    // 创建并显示AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        return rootView;
    }

    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void showFocusTimePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_focus_time_picker, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        TextView tvFocusDuration = dialogView.findViewById(R.id.dialog_text_focus_duration);
        SeekBar seekBarFocusDuration = dialogView.findViewById(R.id.dialog_seek_bar_focus_duration);
        Button btnResetDefault = dialogView.findViewById(R.id.btnResetDefault);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);


        seekBarFocusDuration.setProgress((int)(totalElapsedTime / 60000));
        tvFocusDuration.setText("专注时长：" + (int)(totalElapsedTime / 60000) + "分钟");
        seekBarFocusDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int minutes = progress; // Assuming the minimum is 1 minute.
                tvFocusDuration.setText("专注时长：" + minutes + "分钟");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed.
            }
        });

        btnResetDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarFocusDuration.setProgress((int)(totalElapsedTime / 60000)); // Reset to 25 minutes.
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newFocusDuration = seekBarFocusDuration.getProgress();
                totalElapsedTime = (long) newFocusDuration * 60000;
                timerViewModel.getElapsedTime().setValue(totalElapsedTime);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}