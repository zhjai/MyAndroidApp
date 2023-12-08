package com.example.exam.data;

import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerViewModel extends ViewModel {
    private final MutableLiveData<Long> elapsedTime = new MutableLiveData<>();
    private long totalTime; // 总时间，可以初始化为默认值或0
    private boolean isTimerRunning = false;
    private CountDownTimer timer;

    public TimerViewModel() {
        // 初始化时不再创建CountDownTimer，因为我们需要在startTimer中根据传入的时间创建
    }

    public void startTimer(long totalTime) {
        if (!isTimerRunning) {
            this.totalTime = totalTime; // 更新总时间
            timer = new CountDownTimer(totalTime, 1000) {
                public void onTick(long millisUntilFinished) {
                    elapsedTime.setValue(millisUntilFinished);
                }

                public void onFinish() {
                    isTimerRunning = false;
                    elapsedTime.setValue(0L);
                }
            };
            timer.start();
            isTimerRunning = true;
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        isTimerRunning = false;
    }

    public MutableLiveData<Long> getElapsedTime() {
        return elapsedTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public boolean isTimerRunning() {
        return isTimerRunning;
    }

    // 在ViewModel被清除时停止计时器
    @Override
    protected void onCleared() {
        super.onCleared();
        stopTimer();
    }
}
