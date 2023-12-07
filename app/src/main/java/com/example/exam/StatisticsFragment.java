package com.example.exam;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.exam.data.AwardItem;
import com.example.exam.data.ExampleData;
import com.example.exam.data.GlobalData;
import com.example.exam.data.TaskItem;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    LineChart lineChart; // 折线图
    String[] xValues = new String[12]; // x轴数据
    CustomMarkerView mv;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExampleData.refreshData();
        long startTimeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd");
        for (int i = 6; i >= 0; i--) {
            xValues[i] = simpleDateFormat.format(new Date(startTimeMillis));
            startTimeMillis -= 86400 * 1000L;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        // 折线图
        lineChart = rootView.findViewById(R.id.line_chart);
        mv = new CustomMarkerView(getContext(), R.layout.custom_marker_view_layout);
        drawLineChart();


        return rootView;
    }

    public void drawLineChart() {
        ArrayList<Entry> tasksCount = new ArrayList<>();
        ArrayList<Entry> awardsCount = new ArrayList<>();

        int i = 0;
        for (int countTask : ExampleData.countTask) {
            tasksCount.add(new Entry(i++, countTask));
        }
        i = 0;
        for (int countAward : ExampleData.countAward) {
            awardsCount.add(new Entry(i++, countAward));
        }

        Drawable taskGradient = ContextCompat.getDrawable(getContext(), R.drawable.fade_green); // 自定义的渐变drawable
        Drawable awardGradient = ContextCompat.getDrawable(getContext(), R.drawable.fade_orange); // 自定义的渐变drawable

        LineDataSet taskDataSet = new LineDataSet(tasksCount, "已完成任务");
        taskDataSet.setColor(ContextCompat.getColor(getContext(), R.color.deep_green));
        taskDataSet.setCircleColor(ContextCompat.getColor(getContext(), R.color.deep_green));
        taskDataSet.setLineWidth(2f);
        taskDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 设置为曲线模式
        taskDataSet.setFillDrawable(taskGradient); // 设置渐变
        taskDataSet.setDrawFilled(true);
        taskDataSet.setDrawValues(false);

        LineDataSet awardDataSet = new LineDataSet(awardsCount, "已完成奖励");
        awardDataSet.setColor(ContextCompat.getColor(getContext(), R.color.orange));
        awardDataSet.setCircleColor(ContextCompat.getColor(getContext(), R.color.orange));
        awardDataSet.setLineWidth(2f);
        awardDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 设置为曲线模式
        awardDataSet.setFillDrawable(awardGradient); // 设置渐变
//        awardDataSet.setDrawFilled(true);
        awardDataSet.setDrawValues(false);

        LineData lineData = new LineData(taskDataSet, awardDataSet);
        lineChart.setData(lineData);

        lineChart.getDescription().setEnabled(false);
        lineChart.getAxisLeft().setTextColor(Color.BLACK);
        lineChart.getXAxis().setTextColor(Color.BLACK);
        lineChart.getLegend().setTextColor(Color.BLACK);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateXY(800, 0); // 动画效果
        lineChart.getAxisLeft().enableGridDashedLine(2f, 2f, 0f);
        lineChart.getAxisLeft().setGridColor(Color.GRAY);

        lineChart.setMarker(mv);

        Legend legend = lineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        lineChart.invalidate(); // 刷新图表
    }

    // 点击点时显示的MarkerView
    public class CustomMarkerView extends MarkerView {
        private TextView tvContent;
        private MPPointF mOffset;

        public CustomMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = findViewById(R.id.tvContent);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            tvContent.setText(String.format(Locale.ENGLISH, "%s %d", xValues[(int)e.getX()], (int)e.getY()));
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            if (mOffset == null) {
                // center the marker horizontally and vertically
                mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
            }
            return mOffset;
        }
    }
}