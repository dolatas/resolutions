package com.dododev.resolutions;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dododev.resolutions.model.Resolution;
import com.dododev.resolutions.model.ResolutionStatusDict;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by dodo on 2015-12-14.
 */
@EViewGroup(R.layout.resolutions_drawer_item)
public class ResolutionsDrawerView extends LinearLayout {
    @ViewById
    CheckBox checkBoxStatus;

    public ResolutionsDrawerView(Context context) {
        super(context);
    }

    public void bind(ResolutionStatusDict status) {
        checkBoxStatus.setText(status.toString());
    }
}

