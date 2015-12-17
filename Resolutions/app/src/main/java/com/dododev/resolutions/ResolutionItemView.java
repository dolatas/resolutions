package com.dododev.resolutions;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dododev.resolutions.model.Resolution;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by dodo on 2015-12-14.
 */
@EViewGroup(R.layout.resolution_item)
public class ResolutionItemView extends LinearLayout {
    @ViewById
    TextView title;

    @ViewById
    TextView description;

    public ResolutionItemView(Context context) {
        super(context);
    }

    public void bind(Resolution resolution) {
        title.setText(resolution.getTitle());
        description.setText(resolution.getDescription());
    }
}

