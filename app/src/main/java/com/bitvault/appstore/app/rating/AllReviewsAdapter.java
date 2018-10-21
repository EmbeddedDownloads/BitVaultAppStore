package com.bitvault.appstore.app.rating;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bitvault.appstore.R;
import com.bitvault.appstore.databinding.ItemReviewBinding;
import com.bitvault.appstore.utils.OnLoadMoreListener;
import com.bitvault.appstore.webservice.response.ReviewModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by anuj on 4/5/17.
 * version 1.0.0
 * row of written review of app
 */

public class AllReviewsAdapter extends RecyclerView.Adapter {

    private final static int VIEW_ITEM = 1;
    private final static int VIEW_PROG = 0;
    private final static int VIEW_SPACE = 2;
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ReviewModel> reviewModelsList;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoadMore;
    private int visibleThreshold = 1;
    private String source;

    public AllReviewsAdapter(Context context, final ArrayList<ReviewModel> reviewModelsList, RecyclerView recyclerView, String source) {

        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.reviewModelsList = reviewModelsList;
        this.source = source;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoadMore
                        && totalItemCount <= (lastItem + visibleThreshold) && !recyclerView.isComputingLayout()) {
                    isLoadMore = true;
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }

                }
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            View view = layoutInflater.inflate(R.layout.item_review, parent, false);
            return new ReviewViewHolder(view);
        } else if (viewType == VIEW_PROG) {
            View view = layoutInflater.inflate(R.layout.item_progress_vertical, parent, false);
            return new ProgressViewHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.item_space, parent, false);
            return new SpaceHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ReviewViewHolder) {
            ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;
            if (source.equals(AllReviewsActivity.class.getCanonicalName())) {
                position = position - 1;
            }
            ReviewModel reviewModel = reviewModelsList.get(position);
            if (reviewModel != null) {
                ItemReviewBinding itemReviewBinding = reviewViewHolder.getItemReviewBinding();
                itemReviewBinding.setReviewModel(reviewModel);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(reviewModel.getUpdatedAt()));

                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH) + 1;
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                itemReviewBinding.idTvDate.setText(mDay + "/" + mMonth + "/" + String.valueOf(mYear).substring(2, 4));
                if (reviewModel.getReplyFrom() != null && reviewModel.getReplyFrom().length() > 1) {
                    itemReviewBinding.idTvReplyName.setText(context.getString(R.string.reply_from) + " " + reviewModel.getReplyFrom());


                    calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(Long.parseLong(reviewModel.getReplyupdatedAt()));

                    mYear = calendar.get(Calendar.YEAR);
                    mMonth = calendar.get(Calendar.MONTH) + 1;
                    mDay = calendar.get(Calendar.DAY_OF_MONTH);
                    itemReviewBinding.idTvReplyDate.setText(" " + context.getString(R.string.on) + " " + mDay + "/" + mMonth + "/" + mYear);

                } else {
                    itemReviewBinding.idTvReplyName.setVisibility(View.GONE);
                    itemReviewBinding.idTvReplyDate.setVisibility(View.GONE);
                }
                if (reviewModel.getReplyResponse() == null) {
                    itemReviewBinding.idTvReplyContent.setVisibility(View.GONE);
                }
                itemReviewBinding.idRvRationgBar.setRating(Float.parseFloat(reviewModel.getAppRating()));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (source.equals(AllReviewsActivity.class.getCanonicalName())) {
            return reviewModelsList.size() + 1;
        } else {
            return reviewModelsList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (source.equals(AllReviewsActivity.class.getCanonicalName())) {
            if (position == 0) {
                return VIEW_SPACE;
            } else if (reviewModelsList.get(position - 1) != null) {
                return VIEW_ITEM;
            } else {
                return VIEW_PROG;
            }
        } else {
            if (reviewModelsList.get(position) != null) {
                return VIEW_ITEM;
            } else {
                return VIEW_PROG;
            }
        }
    }

    /**
     * method for set OnLoadMoreListener
     *
     * @param mOnLoadMoreListener
     */
    public void setmOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;

    }

    /**
     * set loadmore variable value
     *
     * @param valuse
     */
    public void setLoading(boolean valuse) {
        isLoadMore = valuse;
    }

    /**
     * remove loader in list
     *
     * @param position
     */
    public void removeItem(int position) {
        if (position > 0) {
            reviewModelsList.remove(position);
            notifyItemChanged(reviewModelsList.size() - 1);
        }
    }

    /**
     * add loader in list
     *
     * @param item
     */
    public void addItem(ReviewModel item) {
        reviewModelsList.add(item);
        notifyItemChanged(reviewModelsList.size() - 1);
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout llLoader;
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            llLoader = (RelativeLayout) v.findViewById(R.id.id_ll_loader);
        }
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private ItemReviewBinding itemReviewBinding;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            itemReviewBinding = DataBindingUtil.bind(itemView);
        }

        public ItemReviewBinding getItemReviewBinding() {
            return itemReviewBinding;
        }
    }

    public class SpaceHolder extends RecyclerView.ViewHolder {

        public SpaceHolder(View itemView) {
            super(itemView);
        }
    }
}
