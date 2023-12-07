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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
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
    BarChart barChart; // 柱状图
    PieChart pieChart; // 饼图
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

        int todayCountTask = ExampleData.countTask.get(ExampleData.countTask.size() - 1);
        int yesterdayCountTask = ExampleData.countTask.get(ExampleData.countTask.size() - 2);
        int todayCountAward = ExampleData.countAward.get(ExampleData.countAward.size() - 1);
        int yesterdayCountAward = ExampleData.countAward.get(ExampleData.countAward.size() - 2);

        TextView textCompletedTasksCount = rootView.findViewById(R.id.text_completed_tasks_count);
        textCompletedTasksCount.setText(String.valueOf(todayCountTask));
        TextView textCompletionTasksAdd = rootView.findViewById(R.id.text_completion_tasks_add);
        if (todayCountTask > yesterdayCountTask) {
            textCompletionTasksAdd.setText(String.format(Locale.ENGLISH, "+%d%%", (int)((todayCountTask - yesterdayCountTask) / (double)yesterdayCountTask * 100)));
        }
        else if (todayCountTask < yesterdayCountTask) {
            textCompletionTasksAdd.setText(String.format(Locale.ENGLISH, "-%d%%", (int)((yesterdayCountTask - todayCountTask) / (double)yesterdayCountTask * 100)));
        }
        else {
            textCompletionTasksAdd.setText("0%");
        }

        TextView textCompletedAwardsCount = rootView.findViewById(R.id.text_completed_awards_count);
        textCompletedAwardsCount.setText(String.valueOf(ExampleData.countAward.get(ExampleData.countAward.size() - 1)));
        TextView textCompletionAwardsAdd = rootView.findViewById(R.id.text_completion_awards_add);
        if (todayCountAward > yesterdayCountAward) {
            textCompletionAwardsAdd.setText(String.format(Locale.ENGLISH, "+%d%%", (int)((todayCountAward - yesterdayCountAward) / (double)yesterdayCountAward * 100)));
        }
        else if (todayCountAward < yesterdayCountAward) {
            textCompletionAwardsAdd.setText(String.format(Locale.ENGLISH, "-%d%%", (int)((yesterdayCountAward - todayCountAward) / (double)yesterdayCountAward * 100)));
        }
        else {
            textCompletionAwardsAdd.setText("0%");
        }


        // 折线图
        lineChart = rootView.findViewById(R.id.line_chart);
        mv = new CustomMarkerView(getContext(), R.layout.custom_marker_view_layout);
        drawLineChart();

        // 柱状图
        barChart = rootView.findViewById(R.id.bar_chart);
        drawBarChart();

        // 饼图
        pieChart = rootView.findViewById(R.id.pie_chart);
        drawPieChart();

        return rootView;
    }

    public void drawLineChart() {
        ArrayList<Entry> tasksCount = new ArrayList<>();
        ArrayList<Entry> awardsCount = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            tasksCount.add(new Entry(i, ExampleData.countTask.get(i)));
            awardsCount.add(new Entry(i, ExampleData.countAward.get(i)));
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

    public void drawBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i, ExampleData.pointsTask.get(i)));
        }

        Drawable gradient = ContextCompat.getDrawable(getContext(), R.drawable.fade_deepgreen_lightgreen); // 自定义的渐变drawable
        BarDataSet barDataSet = new BarDataSet(entries, "已完成任务");
        barDataSet.setDrawValues(false);
        barDataSet.setGradientColor(ContextCompat.getColor(getContext(), R.color.deep_green), ContextCompat.getColor(getContext(), R.color.light_green));

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);
        barChart.getAxisLeft().setTextColor(Color.BLACK);
        barChart.getXAxis().setTextColor(Color.BLACK);
        barChart.getLegend().setTextColor(Color.BLACK);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.animateXY(800, 0); // 动画效果
        barChart.getAxisLeft().enableGridDashedLine(2f, 2f, 0f);
        barChart.getAxisLeft().setGridColor(Color.GRAY);
        barChart.getLegend().setEnabled(false);
        barChart.setMarker(mv);

        barChart.invalidate(); // 刷新图表
    }

    public void drawPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        int taskPoints = 0, AwardPoints = 0;
        for (int i = 0; i < 7; i++) {
            taskPoints += ExampleData.pointsTask.get(i);
            AwardPoints += ExampleData.pointsAward.get(i);
        }
        entries.add(new PieEntry(taskPoints, "已完成任务"));
        entries.add(new PieEntry(AwardPoints, "已完成奖励"));

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setDrawValues(true);
        pieDataSet.setColors(ContextCompat.getColor(getContext(), R.color.deep_green), ContextCompat.getColor(getContext(), R.color.orange));
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setValueLinePart1OffsetPercentage(80.f);
        pieDataSet.setValueLinePart1Length(0.3f);
        pieDataSet.setValueLinePart2Length(0.3f);
        pieDataSet.setValueLineColor(ContextCompat.getColor(getContext(), R.color.teal_200));
        pieDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.deep_green));
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        pieDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int)value);
            }
        });

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(12f);
        pieChart.setData(pieData);

        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setTextColor(Color.BLACK);

        // 图例
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);

        // 调整数据项的显示样式
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(ContextCompat.getColor(getContext(), R.color.deep_green));
        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.animateXY(800, 800); // 动画效果

        pieChart.invalidate(); // 刷新图表
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