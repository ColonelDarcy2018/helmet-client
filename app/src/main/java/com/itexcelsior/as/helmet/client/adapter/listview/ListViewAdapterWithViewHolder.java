package com.itexcelsior.as.helmet.client.adapter.listview;

import android.content.Context;
import android.widget.TextView;

import com.itexcelsior.as.helmet.client.R;
import com.itexcelsior.as.helmet.client.bean.DeviceListViewBean;
import com.itexcelsior.as.helmet.client.util.ListViewConstants;

import java.util.List;


/**
 * Created by smyhvae on 2015/5/4.
 */
public class ListViewAdapterWithViewHolder extends MyListViewAdapter<DeviceListViewBean> {

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public ListViewAdapterWithViewHolder(Context context, List<DeviceListViewBean> datas) {
        super(context, datas, R.layout.device_item_listview);
    }

    @Override
    public void convert(ViewHolder holder, DeviceListViewBean bean) {

        ((TextView) holder.getView(R.id.titleTv)).setText(bean.getTitle());
        TextView phoneTv = (TextView) holder.getView(R.id.phoneTv);
        phoneTv.setText(bean.getPhone());
        if (ListViewConstants.DEVICE_ITEM_LISTVIEW_STATUS_UNCONNECTED.equals(bean.getPhone())){
            phoneTv.setBackgroundColor(holder.getConvertView().getResources().getColor(R.color.app_color_theme_1));
        }

/*
        TextView tv = holder.getView(R.id.titleTv);
        tv.setText(...);

       ImageView view = getView(viewId);
       Imageloader.getInstance().loadImag(view.url);
*/
    }
}
