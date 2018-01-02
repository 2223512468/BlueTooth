package com.jajahome;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Terry} on 2018/1/2.
 */
public class ListAdapter extends BaseAdapter {

    private List<BluetoothDevice> list = new ArrayList<>();
    private Context mContext;

    public ListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addDatas(BluetoothDevice device) {
        for (BluetoothDevice d : list) {
            if (d.getAddress().equals(device.getAddress())) {
                return;
            }
        }
        this.list.add(device);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.act_booth_device_item, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(list.get(position).getName() + ":" + list.get(position).getAddress());
        return convertView;
    }


    class ViewHolder {
        TextView tvName;
    }
}