package com.bitvault.appstore.app.appdetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitvault.appstore.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by anuj on 2/5/17.
 * version 1.0.0
 * Developer detail and app detail like size and version
 */

class AppDescriptionAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<String> appinfo;
    private ArrayList<String> appInfoHeader;
    private DecimalFormat decimalFormat;

    AppDescriptionAdapter(Context context, ArrayList<String> appInfoList, ArrayList<String> appInfoHeader) {
        super();

        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        appinfo = appInfoList;
        this.appInfoHeader = appInfoHeader;
        decimalFormat = new DecimalFormat("##.##");

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.item_app_info, parent, false);
        return new AppInfoHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AppInfoHolder appInfoHolder = (AppInfoHolder) holder;
        appInfoHolder.tvTitle.setText(appInfoHeader.get(position));
        if(appinfo.get(position) != null) {
            if(appInfoHolder.tvTitle.getText().equals(mContext.getString(R.string.updated_on))) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(appinfo.get(position)));

                int mYear = calendar.get(Calendar.YEAR);
                String mMonth = String.format(Locale.US, "%tB", calendar);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                appInfoHolder.tvValue.setText(mDay + "-" + mMonth.substring(0, 3) + "-" + mYear);
            } else if (appInfoHolder.tvTitle.getText().equals(mContext.getString(R.string.download_size))) {
                appInfoHolder.tvValue.setText(decimalFormat.format((float) Long.parseLong(appinfo.get(position)) / (1024 * 1024)) + " " + mContext.getString(R.string.mb));

            } else {
                appInfoHolder.tvValue.setText(appinfo.get(position));
            }
        } else {
            appInfoHolder.tvValue.setText(mContext.getString(R.string.na));
        }

    }

    @Override
    public int getItemCount() {
        return appinfo.size();
    }

    private class AppInfoHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvValue;

        AppInfoHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.id_tv_title);
            tvValue = (TextView) itemView.findViewById(R.id.id_tv_value);
        }
    }
}
