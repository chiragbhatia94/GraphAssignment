package com.chiragbhatia.graphassignmentrevisited;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    /*
    * Code a single screen Android app that,
    * Will use the following hardcoded values
    * X:02 Aug’17 ,Y: 38
    * X:03 Aug’17, Y: 25
    * X:04 Aug’17, Y: 39
    * X:05 Aug’17, Y: 30
    * X:06 Aug’17, Y: 22
    * X:07 Aug’17, Y: 28
    * X:08 Aug’17, Y: 28
    * X:09 Aug’17, Y: 34
    * X:10 Aug’17, Y: 31
    * X:11 Aug’17, Y: 21
    * X:12 Aug’17, Y: 50
    * And plot it on a lifeline graph as shown in the screenshot attached.
    * The graph needs to be horizontally scrollable but not zoomable.
    * */

    private static final String TAG = "MainActivity";

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating view
        TextView xxMin1TV = (TextView) findViewById(R.id.xxMin1TV);
        xxMin1TV.setText(getString(R.string.ph_03) + " ");

        TextView dateTV = (TextView) findViewById(R.id.dateTV);
        dateTV.setText(fromHtml("<strong>12</strong> Aug 2017"));

        TextView xxMin2TV = (TextView) findViewById(R.id.xxMin2TV);
        xxMin2TV.setText(getString(R.string.ph_105) + " ");

        TextView ttdTV = (TextView) findViewById(R.id.ttdTV);
        ttdTV.setText(fromHtml("<strong>" + getString(R.string.total) + "</strong> " + getString(R.string.till_date)));

        // getWindow().setBackgroundDrawableResource(R.drawable.heart);

        TextView title = (TextView) findViewById(R.id.textView);
        title.setText(fromHtml("<strong>Minutes</strong> added to your life till date"));

        GraphView graphView = (GraphView) findViewById(R.id.graphView);

        Map<Date, Integer> dps = new LinkedHashMap<>();

        try {
            dps.put(dateFormat.parse("02/08/17 12:00"), 38);
            dps.put(dateFormat.parse("03/08/17 12:00"), 25);
            dps.put(dateFormat.parse("04/08/17 12:00"), 39);
            dps.put(dateFormat.parse("05/08/17 12:00"), 30);
            dps.put(dateFormat.parse("06/08/17 12:00"), 22);
            dps.put(dateFormat.parse("07/08/17 12:00"), 28);
            dps.put(dateFormat.parse("08/08/17 12:00"), 28);
            dps.put(dateFormat.parse("09/08/17 12:00"), 34);
            dps.put(dateFormat.parse("10/08/17 12:00"), 31);
            dps.put(dateFormat.parse("11/08/17 12:00"), 21);
            dps.put(dateFormat.parse("12/08/17 12:00"), 50);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DataPoint[] dataPoints = generateRandomPoints(dps);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        // series.setColor(getResources().getColor(R.color.white));

        // set date label formatter
        graphView.getGridLabelRenderer().setLabelFormatter(
                new DateAsXAxisLabelFormatter(MainActivity.this));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // set manual x bounds to have nice steps

        try {
            graphView.getViewport().setMinX(dateFormat.parse("02/08/17  12:00").getTime());
            graphView.getViewport().setMaxX(dateFormat.parse("09/08/17 12:00").getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        graphView.getViewport().setXAxisBoundsManual(true);


        // Set them properly if the range is not fixed using the max and min value of the y
        // coords
        graphView.getViewport().setMinY(-52.0);
        graphView.getViewport().setMaxY(52.0);
        graphView.getViewport().setYAxisBoundsManual(true);

        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphView.getViewport().setScrollable(true);

        graphView.addSeries(series);
        // graphView.addSeries(series2);

        // styling series
        series.setColor(Color.BLUE);
        series.setThickness(2);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    private DataPoint[] generateRandomPoints(Map<Date, Integer> map) {
        Map<Date, Double> nmap = new LinkedHashMap<>();
        for (Date dt : map.keySet()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            cal.add(Calendar.HOUR_OF_DAY, -6);
            Double y = Double.valueOf(map.get(dt));

            nmap.put(cal.getTime(), 0.0);

            cal.add(Calendar.HOUR_OF_DAY, 2);

            nmap.put(cal.getTime(), y * 0.05);

            cal.add(Calendar.HOUR_OF_DAY, 2);
            nmap.put(cal.getTime(), -y * 0.35);

            nmap.put(dt, Double.valueOf(map.get(dt)));

            cal.add(Calendar.HOUR_OF_DAY, 5);
            nmap.put(cal.getTime(), -y * 0.90);

            cal.add(Calendar.HOUR_OF_DAY, 2);
            nmap.put(cal.getTime(), 0.0);
        }
        return generateDataPoints(nmap);
    }

    private DataPoint[] generateDataPoints(Map<Date, Double> dps) {
        DataPoint dataPoints[] = new DataPoint[dps.size()];

        int i = 0;
        for (Date date : dps.keySet()) {
            dataPoints[i] = new DataPoint(date, dps.get(date));
            i++;
        }

        return dataPoints;
    }
}
