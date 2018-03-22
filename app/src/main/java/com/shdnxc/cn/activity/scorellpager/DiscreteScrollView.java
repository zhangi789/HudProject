package com.shdnxc.cn.activity.scorellpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.scorellpager.transform.DiscreteScrollItemTransformer;
import com.shdnxc.cn.activity.scorellpager.transform.ScaleTransformer;
import com.shdnxc.cn.activity.scorellpager.util.PicassoRoundTransform;
import com.shdnxc.cn.activity.scorellpager.util.ScrollListenerAdapter;

import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by 张海洋 on 10.04.2017.
 */
@SuppressWarnings("unchecked")
public class DiscreteScrollView extends RecyclerView {

    private static final int DEFAULT_ORIENTATION = Orientation.HORIZONTAL.ordinal();

    private DiscreteScrollLayoutManager layoutManager;

    private ScrollStateChangeListener scrollStateChangeListener;
    private OnItemChangedListener onItemChangedListener;
    private int corner;
    private List<ImageModel> datas;
    private TempAdapter adapter;

    public interface OnItemSelectedListener {
        public void onItemSelected(int index);
    }

    private OnItemSelectedListener listener;

    public DiscreteScrollView(Context context) {
        super(context);
        init(null);
    }

    public void init(final List<ImageModel> datas, OnItemSelectedListener listener) {
        this.listener = listener;
        corner = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.75 * 0.08);
        this.datas = datas;
        setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(1.0f)
                .build());
        adapter = new TempAdapter();
        setScrollStateChangeListener(new ScrollStateChangeListener<ViewHolder>() {
            @Override
            public void onScrollStart(@NonNull ViewHolder currentItemHolder, int adapterPosition) {

            }

            @Override
            public void onScrollEnd(@NonNull ViewHolder currentItemHolder, int adapterPosition) {
                if (adapterPosition > datas.size() * 2) {
                    scrollToPosition(datas.size() + adapterPosition % datas.size());
                }
            }

            @Override
            public void onScroll(float scrollPosition, @NonNull ViewHolder currentHolder, @NonNull ViewHolder newCurrent) {

            }
        });

        setOffscreenItems(3);
        setAdapter(adapter);
    }


    class TempAdapter extends Adapter<TempViewHolder> {
        @Override
        public TempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_page, null);
            return new TempViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TempViewHolder holer, final int position) {
            final int realPosition = position % datas.size();
            ImageModel item = datas.get(realPosition);
            holer.container.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < datas.size(); i++) {
                        ImageModel model = datas.get(i);
                        model.setChecked(realPosition == i);
                    }
                    adapter.notifyDataSetChanged();
                    if (listener != null) {
                        listener.onItemSelected(realPosition);
                    }
                }
            });

            Picasso.with(getContext()).load(item.getImage()).transform(new PicassoRoundTransform(corner)).into(holer.imageView);
            if (item.getChecked()) {
                holer.tagImageview.setVisibility(View.VISIBLE);
            } else {
                holer.tagImageview.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onBindViewHolder(TempViewHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public int getItemCount() {
            return datas.size() * 5;
        }
    }


    class TempViewHolder extends ViewHolder {
        public FrameLayout container;
        public ImageView tagImageview, imageView;

        public TempViewHolder(View itemView) {
            super(itemView);
            container = ((FrameLayout) itemView.findViewById(R.id.container));
            tagImageview = ((ImageView) itemView.findViewById(R.id.tagImageView));
            imageView = ((ImageView) itemView.findViewById(R.id.imageView));
        }
    }


    public DiscreteScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DiscreteScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int orientation = DEFAULT_ORIENTATION;
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.DiscreteScrollView);
            orientation = ta.getInt(R.styleable.DiscreteScrollView_dsv_orientation, DEFAULT_ORIENTATION);
            ta.recycle();
        }

        layoutManager = new DiscreteScrollLayoutManager(
                getContext(), new ScrollStateListener(),
                Orientation.values()[orientation]);
        setLayoutManager(layoutManager);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        int percentWidth = (int) (width * 0.75);
        setMeasuredDimension(width, percentWidth / 2);

    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof DiscreteScrollLayoutManager) {
            super.setLayoutManager(layout);
        } else {
            throw new IllegalArgumentException("DiscreteScrollLayoutManager");
        }
    }


    @Override
    public boolean fling(int velocityX, int velocityY) {
        boolean isFling = super.fling(velocityX, velocityY);
        if (isFling) {
            layoutManager.onFling(velocityX, velocityY);
        } else {
            layoutManager.returnToCurrentPosition();
        }
        return isFling;
    }

    @Nullable
    public ViewHolder getViewHolder(int position) {
        View view = layoutManager.findViewByPosition(position);
        return view != null ? getChildViewHolder(view) : null;
    }

    /**
     * @return adapter position of the current item or -1 if nothing is selected
     */
    public int getCurrentItem() {
        return layoutManager.getCurrentPosition();
    }

    public void setItemTransformer(DiscreteScrollItemTransformer transformer) {
        layoutManager.setItemTransformer(transformer);
    }

    public void setItemTransitionTimeMillis(@IntRange(from = 10) int millis) {
        layoutManager.setTimeForItemSettle(millis);
    }

    public void setOrientation(Orientation orientation) {
        layoutManager.setOrientation(orientation);
    }

    public void setOffscreenItems(int items) {
        layoutManager.setOffscreenItems(items);
    }

    public void setScrollStateChangeListener(ScrollStateChangeListener<?> scrollStateChangeListener) {
        this.scrollStateChangeListener = scrollStateChangeListener;
    }

    public void setScrollListener(ScrollListener<?> scrollListener) {
        setScrollStateChangeListener(new ScrollListenerAdapter(scrollListener));
    }

    public void setOnItemChangedListener(OnItemChangedListener<?> onItemChangedListener) {
        this.onItemChangedListener = onItemChangedListener;
    }

    private class ScrollStateListener implements DiscreteScrollLayoutManager.ScrollStateListener {

        @Override
        public void onIsBoundReachedFlagChange(boolean isBoundReached) {
            setOverScrollMode(isBoundReached ? OVER_SCROLL_ALWAYS : OVER_SCROLL_NEVER);
        }

        @Override
        public void onScrollStart() {
            if (scrollStateChangeListener != null) {
                int current = layoutManager.getCurrentPosition();
                ViewHolder holder = getViewHolder(current);
                if (holder != null) {
                    scrollStateChangeListener.onScrollStart(holder, current);
                }
            }
        }

        @Override
        public void onScrollEnd() {
            ViewHolder holder = null;
            int current = layoutManager.getCurrentPosition();
            if (scrollStateChangeListener != null) {
                holder = getViewHolder(current);
                if (holder == null) {
                    return;
                }
                scrollStateChangeListener.onScrollEnd(holder, current);
            }
            if (onItemChangedListener != null) {
                if (holder == null) {
                    holder = getViewHolder(current);
                }
                if (holder != null) {
                    onItemChangedListener.onCurrentItemChanged(holder, current);
                }
            }
        }

        @Override
        public void onScroll(float currentViewPosition) {
            if (scrollStateChangeListener != null) {
                int current = getCurrentItem();
                ViewHolder currentHolder = getViewHolder(getCurrentItem());

                int newCurrent = current + (currentViewPosition < 0 ? 1 : -1);
                ViewHolder newCurrentHolder = getViewHolder(newCurrent);

                if (currentHolder != null && newCurrentHolder != null) {
                    scrollStateChangeListener.onScroll(
                            currentViewPosition, currentHolder,
                            newCurrentHolder);
                }
            }
        }

        @Override
        public void onCurrentViewFirstLayout() {
            if (onItemChangedListener != null) {
                int current = layoutManager.getCurrentPosition();
                ViewHolder currentHolder = getViewHolder(current);
                if (currentHolder != null) {
                    onItemChangedListener.onCurrentItemChanged(currentHolder, current);
                }
            }
        }
    }

    public interface ScrollStateChangeListener<T extends ViewHolder> {

        void onScrollStart(@NonNull T currentItemHolder, int adapterPosition);

        void onScrollEnd(@NonNull T currentItemHolder, int adapterPosition);

        void onScroll(float scrollPosition, @NonNull T currentHolder, @NonNull T newCurrent);
    }

    public interface ScrollListener<T extends ViewHolder> {

        void onScroll(float scrollPosition, @NonNull T currentHolder, @NonNull T newCurrent);
    }

    public interface OnItemChangedListener<T extends ViewHolder> {
        /*
         * This method will be also triggered when view appears on the screen for the first time.
         */
        void onCurrentItemChanged(@NonNull T viewHolder, int adapterPosition);

    }
}
