package org.smartregister.goldsmith.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.smartregister.goldsmith.R;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 14-10-2020.
 */
public class LaunchpadView extends LinearLayout {

    private String itemTitle;
    private int itemIcon;
    private String itemDescription;

    private TextView itemTitleTv;
    private TextView itemDescriptionTv;
    private ImageView itemIconIv;
    private float itemIconSize;

    public LaunchpadView(Context context) {
        super(context);
        initView(context, null);
    }

    public LaunchpadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public LaunchpadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public LaunchpadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attributeSet) {
        View.inflate(context, R.layout.view_launchpad_item, this);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.LaunchpadView,
                0, 0);

        try {
            itemTitle = a.getString(R.styleable.LaunchpadView_itemTitle);
            itemDescription = a.getString(R.styleable.LaunchpadView_itemDescription);
            itemIcon = a.getResourceId(R.styleable.LaunchpadView_itemIcon, 0);
            itemIconSize = a.getDimensionPixelSize(R.styleable.LaunchpadView_itemIconSize, 0);
        } finally {
            a.recycle();
        }

        itemTitleTv = findViewById(R.id.launchpadItem_title);
        itemDescriptionTv = findViewById(R.id.launchpadItem_description);
        itemIconIv = findViewById(R.id.launchpadItem_image);

        renderDetails();
    }

    private void renderDetails() {
        if (getItemTitle() != null) {
            itemTitleTv.setText(getItemTitle());
        }

        if (getItemDescription() != null) {
            itemDescriptionTv.setVisibility(VISIBLE);
            itemDescriptionTv.setText(getItemDescription());
        } else {
            itemDescriptionTv.setVisibility(GONE);
        }

        if (getItemIcon() != 0) {
            itemIconIv.setImageDrawable(getContext().getDrawable(getItemIcon()));
        }

        if (getItemIconSize() != 0) {
            LayoutParams params = (LayoutParams) itemIconIv.getLayoutParams();
            params.width = (int) getItemIconSize();
            params.height = (int) getItemIconSize();
            itemIconIv.setLayoutParams(params);
        }
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
        renderDetails();
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public float getItemIconSize() {
        return itemIconSize;
    }

    public void setItemIconSize(float itemIconSize) {
        this.itemIconSize = itemIconSize;
    }
}
