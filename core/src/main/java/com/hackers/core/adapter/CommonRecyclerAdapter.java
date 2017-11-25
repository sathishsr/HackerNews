package com.hackers.core.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by SR on 25/11/17.
 */

public class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<CommonRecyclerAdapter.ViewHolder> implements Filterable {

    private final TypedValue mTypedValue = new TypedValue();
    private List<T> elements;
    private Context mContext;
    private int layoutID = 0;
    private int[] viewIds;
    private int lastSelectedPosition;
    private int selectedRowBGColor = -1;
    private int unSelectedRowBGColor = Color.parseColor("#00000000");
    private int selectedRowBGResource = -1;
    private int unSelectedRowBGResource = -1;
    private int oddRowColor = -1;
    private int evenRowColor = -1;
    private int mBackground;
    private PopulationListener<T> listener;
    private OnRowCreateListener<T> rowListener;
    private CommonFilter commonFilter;
    private boolean reverseOrder;
    private int lastPosition=-1;


    public CommonRecyclerAdapter(Context context, final List<T> elem) {
//        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mContext = context;
        mBackground = mTypedValue.resourceId;
        this.mContext = context;
        this.elements = elem;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        ViewHolder holder = null;
        if (view == null) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(layoutID, parent, false);
            view.setBackgroundResource(mBackground);
            holder = new ViewHolder(view, viewIds);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        if (listener != null) {
            listener.onRowCreate(holder.getViews());
        }
        if (rowListener != null) {
            rowListener.onRowCreate(holder.mView, position, elements.get(position),
                    holder.getViews());
        }

        // Set alternative row color
        if (oddRowColor != -1) {
            if (position % 2 == 1) {
                holder.mView.setBackgroundColor(ContextCompat.getColor(mContext, oddRowColor));
            } else {
//                holder.mView.setBackgroundColor(evenRowColor);
                holder.mView.setBackgroundColor(ContextCompat.getColor(mContext, evenRowColor));
            }
        }

        // Selected row
        if (selectedRowBGColor != -1) {
            if (lastSelectedPosition == position) {
                // Selected Row Color
                holder.mView.setBackgroundColor(selectedRowBGColor);
            } else {
                // UnSelected Row Color
                holder.mView.setBackgroundColor(unSelectedRowBGColor);
            }
        } else if (selectedRowBGResource != -1) {
            if (lastSelectedPosition == position) {
                // Selected Row Resource
                holder.mView.setBackgroundResource(selectedRowBGResource);
            } else {
                // UnSelected Row Resource
//                holder.mView.setBackgroundResource(unSelectedRowBGResource);
            }
        }

        /**
         * Here we store the position value with key as layoutID to handle
         * alternate colors for list rows.
         *
         * please refer OnItemClickListener (onListClick in watch list) for
         * listView.
         *
         */
        holder.mView.setTag(layoutID, position);

        if (listener != null) {
            listener.populateFrom(holder.mView, position, elements.get(position),
                    holder.getViews());
        }

        // Here you apply the animation when the view is bound
//        setAnimation(holder.itemView, position);
    }

    public void setLayoutTextViews(int layoutID, int[] viewIDs) {
        this.layoutID = layoutID;
        this.viewIds = viewIDs;
    }

    public void setLayoutTextViews(int layoutID) {
        this.layoutID = layoutID;
    }

    public void clear() {
        elements.clear();
    }

    public void add(final T view) {
        elements.add(view);
    }

    public void add(int position, final T view) {
        elements.add(position, view);
    }

    public void set(int position, final T view) {
        elements.set(position, view);
    }

    public void addAll(final List<T> rows) {
        elements.addAll(rows);
    }

    public void reverseOrder() {
        Collections.reverse(elements);
    }

    public void set(final List<T> rows) {
        elements = rows;
    }

    public void remove(int position) {
        elements.remove(position);
    }

    public void remove(T item) {
        elements.remove(item);
    }

    public int getCount() {
        return elements.size();
    }

    public List<T> getAll() {
        return elements;
    }

    public T getItem(int position) {
        if (getCount() > 0)
            return elements.get(position);
        return null;
    }

    public List<T> getItems() {
        return elements;
    }

    public long getItemId(int position) {
        return position;
    }

    public void setPopulationListener(PopulationListener<T> listener) {
        this.listener = listener;
    }

    public void setOnRowCreateListener(OnRowCreateListener<T> listener) {
        this.rowListener = listener;
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public void setLastSelectedPosition(int position) {
        lastSelectedPosition = position;
    }

    public void setRowBackgroundColor(int selectedRowBGColor,
                                      int unSelectedRowBGColor) {
        this.selectedRowBGColor = selectedRowBGColor;
        this.unSelectedRowBGColor = unSelectedRowBGColor;
    }

    public void setRowBackgroundResource(int selectedRowBGResource,
                                         int unSelectedRowBGResource) {
        this.selectedRowBGResource = selectedRowBGResource;
        this.unSelectedRowBGResource = unSelectedRowBGResource;
    }

    public void setAlternativeRowColor(int oddRowColor, int evenRowColor) {
        this.oddRowColor = oddRowColor;
        this.evenRowColor = evenRowColor;
    }

    @Override
    public Filter getFilter() {
        return commonFilter;
    }

    public void setFilter(CommonFilter filter) {
        this.commonFilter = filter;
    }

    public interface CommonFilterListener {
        void setFilter();
    }

    public interface OnRowCreateListener<T> {

        void onRowCreate(View v, int position, final T view, View[] views);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        private View[] views;


        public ViewHolder(View view, int[] viewIds) {
            super(view);
            mView = view;
            if (mView != null && viewIds != null) {
                this.views = new View[viewIds.length];

                for (int i = 0; i < viewIds.length; ++i) {
                    this.views[i] = mView.findViewById(viewIds[i]);
                }

            }
        }

        public View[] getViews() {
            return this.views;
        }


        public void clearAnimation()
        {
            mView.clearAnimation();
        }

    }

    public class CommonFilter extends Filter {

        private final CommonRecyclerAdapter adapter;

        private final List<T> originalList;

        private final List<T> filteredList;


        private CommonFilter(CommonRecyclerAdapter adapter, List<T> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList.clear();
            filteredList.addAll((ArrayList<T>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {

    }

    @Override
    public void onViewDetachedFromWindow(CommonRecyclerAdapter.ViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.clearAnimation();
    }
}
