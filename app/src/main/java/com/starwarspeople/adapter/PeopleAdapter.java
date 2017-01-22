package com.starwarspeople.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.starwarspeople.R;
import com.starwarspeople.data.model.PeopleResponse;
import com.starwarspeople.data.model.PlanetResponse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Divya on 1/22/2017.
 */

public class PeopleAdapter extends BaseAdapter<PeopleResponse.Result> implements Filterable {

    private FooterViewHolder footerViewHolder;
    private PlanetFilter planetFilter;

    public PeopleAdapter() {
        super();
    }

    @Override
    public int getItemViewType(int position) {
        return (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }

    @Override
    protected RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item_row, parent, false);

        final VideoViewHolder holder = new VideoViewHolder(v);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(adapterPos, holder.itemView);
                    }
                }
            }
        });

        return holder;
    }

    @Override
    protected RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_footer, parent, false);

        final FooterViewHolder holder = new FooterViewHolder(v);
        holder.reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onReloadClickListener != null) {
                    onReloadClickListener.onReloadClick();
                }
            }
        });

        return holder;
    }

    @Override
    protected void bindHeaderViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    protected void bindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final VideoViewHolder holder = (VideoViewHolder) viewHolder;

        final PeopleResponse.Result people = getItem(position);
        if (people != null) {
            holder.bind(people);
        }
    }

    @Override
    protected void bindFooterViewHolder(RecyclerView.ViewHolder viewHolder) {
        FooterViewHolder holder = (FooterViewHolder) viewHolder;
        footerViewHolder = holder;
    }

    @Override
    protected void displayLoadMoreFooter() {
        if (footerViewHolder != null) {
            footerViewHolder.errorRelativeLayout.setVisibility(View.GONE);
            footerViewHolder.loadingFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void displayErrorFooter() {
        if (footerViewHolder != null) {
            footerViewHolder.loadingFrameLayout.setVisibility(View.GONE);
            footerViewHolder.errorRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void addFooter() {
        isFooterAdded = true;
        add(new PeopleResponse().new Result());
    }

    @Override
    public Filter getFilter() {
        if(planetFilter == null)
            planetFilter = new PlanetFilter(this, getAllItems());
        return planetFilter;
    }

    private static class PlanetFilter extends Filter {

        private final PeopleAdapter adapter;

        private final List<PeopleResponse.Result> originalList;

        private final List<PeopleResponse.Result> filteredList;

        private PlanetFilter(PeopleAdapter adapter, List<PeopleResponse.Result> originalList) {
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

                for (final PeopleResponse.Result user : originalList) {
                    if (user.getHomeworld().equals(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.getAllItems().clear();
            adapter.getAllItems().addAll((ArrayList<PeopleResponse.Result>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
    
    // region Inner Classes

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.full_name)
        TextView fullName;
        @BindView(R.id.birth_year)
        TextView birthYear;
        @BindView(R.id.homeworld_name)
        TextView homeWorld;


        // region Constructors
        public VideoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        // endregion

        // region Helper Methods
        private void bind(PeopleResponse.Result people) {
            setUpTitle(fullName, people);
            setBirthYear(birthYear, people);
            setUpHomeWorld(homeWorld, people);
        }

        private void setUpTitle(TextView tv, PeopleResponse.Result people) {
            String name = people.getName();
            if (!TextUtils.isEmpty(name)) {
                tv.setText(name);
            }
        }

        private void setBirthYear(TextView tv, PeopleResponse.Result people) {
            String birthYear = people.getBirthYear();
            if (!TextUtils.isEmpty(birthYear)) {
                tv.setText(birthYear);
            }
        }

        private void setUpHomeWorld(TextView tv, PeopleResponse.Result people) {
            String homeworld = people.getHomeworld();
            if (!TextUtils.isEmpty(homeworld)) {
                tv.setText(homeworld);
            }
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        // region Views
        @BindView(R.id.loading_fl)
        FrameLayout loadingFrameLayout;
        @BindView(R.id.error_rl)
        RelativeLayout errorRelativeLayout;
        @BindView(R.id.loading_iv)
        ProgressBar loadingProgress;
        @BindView(R.id.reload_btn)
        Button reloadButton;
        // endregion

        // region Constructors
        public FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        // endregion
    }

    // endregion

}
