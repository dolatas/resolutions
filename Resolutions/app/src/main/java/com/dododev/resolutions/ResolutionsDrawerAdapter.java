package com.dododev.resolutions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dododev.resolutions.dao.ResolutionDao;
import com.dododev.resolutions.dao.impl.ResolutionDaoImpl;
import com.dododev.resolutions.model.Resolution;
import com.dododev.resolutions.model.ResolutionStatusDict;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by dodo on 2015-12-14.
 */
@EBean
public class ResolutionsDrawerAdapter extends BaseAdapter {

    @RootContext
    Context context;

    @Override
    public int getCount() {
        return ResolutionStatusDict.values().length;
    }

    @Override
    public ResolutionStatusDict getItem(int i) {
        return ResolutionStatusDict.values()[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ResolutionsDrawerView resolutionsDrawerView;
        if (view == null) {
            resolutionsDrawerView = ResolutionsDrawerView_.build(context);
        } else {
            resolutionsDrawerView = (ResolutionsDrawerView) view;
        }

        resolutionsDrawerView.bind(getItem(i));

        return resolutionsDrawerView;
    }
}
