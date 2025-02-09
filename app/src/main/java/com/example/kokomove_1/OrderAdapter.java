package com.example.kokomove_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<UploadOrder> dataList;

    public OrderAdapter(Context context, List<UploadOrder> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.img_list_picture = convertView.findViewById(R.id.img_list_picture);
            viewHolder.text_list_orderNum = convertView.findViewById(R.id.text_list_orderNum);
            viewHolder.text_list_thing = convertView.findViewById(R.id.text_list_thing);
            viewHolder.text_list_receiveStatus = convertView.findViewById(R.id.text_list_receiveStatus);
            viewHolder.text_list_sendStatus = convertView.findViewById(R.id.text_list_sendStatus);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(dataList.get(position).getSendMethod().equals("機車")){
            viewHolder.img_list_picture.setImageResource(R.drawable.scooter);
        }else if(dataList.get(position).getSendMethod().equals("廂型貨車")){
            viewHolder.img_list_picture.setImageResource(R.drawable.van);
        }else if(dataList.get(position).getSendMethod().equals("貨車")){
            viewHolder.img_list_picture.setImageResource(R.drawable.truck);
        }else {
            viewHolder.img_list_picture.setImageResource(R.drawable.drone);
        }

        viewHolder.text_list_orderNum.setText(dataList.get(position).getOrderNumber());
        viewHolder.text_list_thing.setText(dataList.get(position).getThing());
        if(dataList.get(position).getReceiveTime().isEmpty()){
            viewHolder.text_list_receiveStatus.setText("未收貨");
        }else {
            viewHolder.text_list_receiveStatus.setText("已收貨");
        }

        if(dataList.get(position).getSendTime().isEmpty()){
            viewHolder.text_list_sendStatus.setText("未送達");
        }else {
            viewHolder.text_list_sendStatus.setText("已送達");
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView img_list_picture;
        TextView text_list_orderNum;
        TextView text_list_thing;
        TextView text_list_receiveStatus;
        TextView text_list_sendStatus;
    }
}
