package ca.birthalert.aronne.birthalert;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

/**
 * Created by Aronne on 12/07/2015.
 */
public class LiveDisplay extends ActionBarActivity implements OnClickListener {
    private Button refresh;

    private static final int HISTORY_SIZE = 20;

    private XYPlot tempPlot = null;

    private SimpleXYSeries tempSeries;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_display);

        tempPlot = (XYPlot) findViewById(R.id.tempPlot);

        tempSeries = new SimpleXYSeries("Temperature");
        tempSeries.useImplicitXVals();

        tempPlot.getGraphWidget().getDomainGridLinePaint().setColor(Color.MAGENTA);
        tempPlot.getGraphWidget().getRangeSubGridLinePaint().setColor(Color.MAGENTA);
        tempPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        tempPlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        tempPlot.setRangeBoundaries(0, 35, BoundaryMode.FIXED);
        tempPlot.setDomainBoundaries(0, 20, BoundaryMode.FIXED);
        tempPlot.addSeries(tempSeries, new LineAndPointFormatter((Color.rgb(200, 10, 10)), Color.RED, null, null));
        tempPlot.setDomainStepValue(5);
        tempPlot.setTicksPerRangeLabel(3);
        tempPlot.setDomainLabel("Time");
        tempPlot.getDomainLabelWidget().pack();
        tempPlot.setRangeLabel("Temperature");
        tempPlot.getRangeLabelWidget().pack();

        if(tempSeries.size()>HISTORY_SIZE){
            tempSeries.removeFirst();
        }

        refresh = (Button) findViewById(R.id.bStart);
        refresh.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        for(int i=0; i<((DataArrayApplication) getApplicationContext()).myTempData.size();i++){
            Double tempArray = ((DataArrayApplication) getApplicationContext()).myTempData.get(i);

            tempSeries.addLast(null,tempArray);}

        tempPlot.redraw();



    }
}
