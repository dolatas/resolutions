package com.dododev.resolutions;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dododev.resolutions.model.Resolution;
import com.dododev.resolutions.utils.Constants;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;

/**
 * Created by dodo on 2015-12-14.
 */
@EViewGroup(R.layout.resolution_item)
public class ResolutionItemView extends LinearLayout {
    @ViewById
    TextView title;

    @ViewById
    TextView description;

    @ViewById
    TextView startDate;

    @ViewById
    TextView endDate;

    private static final SimpleDateFormat SDF = new SimpleDateFormat(Constants.DATE_FORMAT_DAY);

    public ResolutionItemView(Context context) {
        super(context);
    }

    public void bind(Resolution resolution) {
        title.setText(resolution.getTitle());
        description.setText(resolution.getDescription());
        if(resolution.getStartDate() != null) {
            startDate.setText(SDF.format(resolution.getStartDate()));
        }
        if(resolution.getEndDate() != null) {
            endDate.setText(SDF.format(resolution.getEndDate()));
        }
    }
}

