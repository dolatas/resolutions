package com.dododev.resolutions;

import android.content.Context;
import android.widget.ImageView;
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

    @ViewById
    ImageView statusImg;

    public ResolutionItemView(Context context) {
        super(context);
    }

    public void bind(Resolution resolution) {
        title.setText(resolution.getTitle());
        description.setText(resolution.getDescription());
        if(resolution.getStatus() != null){
            switch (resolution.getStatus()){
                case PENDING:
                    statusImg.setImageResource(R.drawable.pending);
                    break;
                case ONGOING:
                    statusImg.setImageResource(R.drawable.ongoing);
                    break;
                case SUCCESS:
                    statusImg.setImageResource(R.drawable.success);
                    break;
                case FAILURE:
                    statusImg.setImageResource(R.drawable.failure);
                    break;
                case UNKNOWN:
                    statusImg.setImageResource(R.drawable.unknown);
                    break;
            }
        }
    }
}

