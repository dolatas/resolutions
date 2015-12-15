package com.dododev.resolutions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dododev.resolutions.dao.ResolutionDao;
import com.dododev.resolutions.dao.impl.ResolutionDaoImpl;
import com.dododev.resolutions.model.Resolution;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by dodo on 2015-12-14.
 */
@EBean
public class ResolutionListAdapter extends BaseAdapter {
    List<Resolution> resolutions;

    @Bean(ResolutionDaoImpl.class)
    ResolutionDao resolutionDao;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        resolutions = resolutionDao.findAll();
    }

    @Override
    public int getCount() {
        return resolutions.size();
    }

    @Override
    public Resolution getItem(int i) {
        return resolutions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ResolutionItemView resolutionItemView;
        if (view == null) {
            resolutionItemView = ResolutionItemView_.build(context);
        } else {
            resolutionItemView = (ResolutionItemView) view;
        }

        resolutionItemView.bind(getItem(i));

        return resolutionItemView;
    }
}
