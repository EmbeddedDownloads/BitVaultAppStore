package com.bitvault.appstore.app.appdetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitvault.appstore.R;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.response.AppPermissions;

import java.util.List;

/**
 * Created Dheeraj Bansal root on 12/5/17.
 * version 1.0.0
 */

/**
 * permission adapter will show all permission taken by app
 */

public class PermissionAdapter extends RecyclerView.Adapter<PermissionAdapter.PermissionViewHolder> {

    private List<AppPermissions.Permission> permissionList;
    private Context mContext;

    private boolean isLayoutChange;

    public PermissionAdapter(List<AppPermissions.Permission> permissionList, Context mContext) {
        this.permissionList = permissionList;
        this.mContext = mContext;
    }


    @Override
    public PermissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_permission, parent, false);
        return new PermissionViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final PermissionViewHolder holder, final int listPosition) {

        AppPermissions.Permission item = permissionList.get(listPosition);
        holder.setIcon(item.getGroup_permission());
        holder.setPermissionName(item.getName());
        holder.setPermissionDetail(item.getDetail());

    }

    @Override
    public int getItemCount() {
        return permissionList.size();
    }

    public boolean isLayoutChange() {
        return isLayoutChange;
    }

    public void setLayoutChange(boolean layoutChange) {
        isLayoutChange = layoutChange;
    }

    public class PermissionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout mainRelativeLayout;
        private ImageView permissionIcon;
        private TextView permissionNameTxt;
        private TextView permissionDetailTxt;
        private TextView permissionArrowTxt;


        public PermissionViewHolder(View itemView) {
            super(itemView);
            this.mainRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.id_permission_main_lout);
            this.permissionIcon = (ImageView) itemView.findViewById(R.id.id_permission_icon);
            this.permissionNameTxt = (TextView) itemView.findViewById(R.id.id_permission_name);
            this.permissionDetailTxt = (TextView) itemView.findViewById(R.id.id_permission_detail);
            this.permissionArrowTxt = (TextView) itemView.findViewById(R.id.id_pmn_arrow_txt);
            this.mainRelativeLayout.setOnClickListener(this);
        }

        /**
         * set permission name
         *
         * @param name
         */
        private void setPermissionName(String name) {
            if (name != null) {
                permissionNameTxt.setText(name);
            }
        }

        /**
         * set permission details
         *
         * @param detail
         */
        private void setPermissionDetail(String detail) {
            if (detail != null) {
                permissionDetailTxt.setText(detail);
            }
        }

        /**
         * set app icon
         *
         * @param groupPermission
         */
        private void setIcon(String groupPermission) {
            if (groupPermission != null && groupPermission.length() > 1) {
                permissionIcon.setImageDrawable(Utils.getPermissionDrawable(mContext, groupPermission));
            }
        }


        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.id_permission_main_lout) {
                if (permissionDetailTxt.getVisibility() == View.VISIBLE) {
                    permissionDetailTxt.setVisibility(View.GONE);
                    permissionArrowTxt.setRotation(270);
                    setLayoutChange(true);
                } else {
                    permissionDetailTxt.setVisibility(View.VISIBLE);
                    permissionArrowTxt.setRotation(90);
                    setLayoutChange(true);
                }
            }
        }
    }
}