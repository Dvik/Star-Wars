package com.starwarspeople.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.starwarspeople.R;
import com.starwarspeople.connection.SendFilterData;
import com.starwarspeople.adapter.PeopleAdapter;
import com.starwarspeople.data.model.PeopleResponse;
import com.starwarspeople.retrofit.PeopleAPI;
import com.starwarspeople.retrofit.rest.PeopleDataClient;
import com.starwarspeople.utility.NetworkLogUtility;
import com.starwarspeople.utility.NetworkUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Divya on 1/22/2017.
 */
public class PeopleFragment extends Fragment implements PeopleAdapter.OnReloadClickListener,SendFilterData {

    private RecyclerView recyclerView;
    private ProgressBar loadingProgress;
    private TextView errorTextView;
    private LinearLayout errorLinearLayout;
    private LinearLayoutManager layoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private PeopleAPI apiService;
    private int currentPage = 1;
    private PeopleAdapter peoplesAdapter;
    private Unbinder unbinder;
    private String type;
    private TreeSet<String> planetList;

    @Override
    public void sendData(String data) {
        peoplesAdapter.getFilter().filter(data);
    }

    public interface ListDataChangeListener {
        public void onDataChangeListener(TreeSet<String> s);
    }

    public ListDataChangeListener dataChangeListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;

            try {
                dataChangeListener = (ListDataChangeListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement listener");
            }
        }
    }


    public PeopleFragment() {
        // Required empty public constructor
    }

    public static PeopleFragment newInstance() {
        return new PeopleFragment();
    }

    public static PeopleFragment newInstance(Bundle extras) {
        PeopleFragment fragment = new PeopleFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = PeopleDataClient.getClient().create(PeopleAPI.class);
        planetList = new TreeSet<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_people, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_male);
        loadingProgress = (ProgressBar) rootView.findViewById(R.id.loading_iv);
        errorLinearLayout = (LinearLayout) rootView.findViewById(R.id.error_ll);
        errorTextView = (TextView) rootView.findViewById(R.id.error_tv);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= 10) {
                    loadMoreItems();
                }
            }
        }
    };

    private Callback<PeopleResponse> findVideosFirstFetchCallback = new Callback<PeopleResponse>() {
        @Override
        public void onResponse(Call<PeopleResponse> call, Response<PeopleResponse> response) {
            loadingProgress.setVisibility(View.GONE);
            isLoading = false;

            if (!response.isSuccessful()) {
                int responseCode = response.code();
                if (responseCode == 504) {
                    errorLinearLayout.setVisibility(View.VISIBLE);
                    errorTextView.setText("Can't load data.\nCheck your network connection.");
                }
                return;
            }

            PeopleResponse peopleResponse = response.body();
            if (peopleResponse != null) {
                List<PeopleResponse.Result> peoples = peopleResponse.getResults();
                List<PeopleResponse.Result> filteredPeoples = new ArrayList<>();
                for (PeopleResponse.Result a : peoples) {
                    if (a.getGender().equalsIgnoreCase(type)) {
                        filteredPeoples.add(a);
                    }
                    planetList.add(a.getHomeworld());
                    dataChangeListener.onDataChangeListener(planetList);
                }
                if (filteredPeoples.size() > 0)
                    peoplesAdapter.addAll(filteredPeoples);

                if (peoples.size() >= 10) {
                    peoplesAdapter.addFooter();
                    loadMoreItems();
                } else {
                    isLastPage = true;
                }
            }
        }

        @Override
        public void onFailure(Call<PeopleResponse> call, Throwable t) {
            NetworkLogUtility.logFailure(call, t);

            if (!call.isCanceled()) {
                isLoading = false;
                loadingProgress.setVisibility(View.GONE);

                if (NetworkUtility.isKnownException(t)) {
                    errorTextView.setText("Can't load data.\nCheck your network connection.");
                    errorLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    private Callback<PeopleResponse> findVideosNextFetchCallback = new Callback<PeopleResponse>() {
        @Override
        public void onResponse(Call<PeopleResponse> call, Response<PeopleResponse> response) {
            peoplesAdapter.removeFooter();
            isLoading = false;

            if (!response.isSuccessful()) {
                int responseCode = response.code();
                switch (responseCode) {
                    case 504:
                        break;
                    case 400:
                        isLastPage = true;
                        break;
                }
                return;
            }

            PeopleResponse peoplesEnvelope = response.body();
            if (peoplesEnvelope != null) {
                List<PeopleResponse.Result> peoples = peoplesEnvelope.getResults();
                List<PeopleResponse.Result> filteredPeoples = new ArrayList<>();
                for (PeopleResponse.Result a : peoples) {
                    if (a.getGender().equalsIgnoreCase(type)) {
                        filteredPeoples.add(a);
                    }
                    planetList.add(a.getHomeworld());
                    dataChangeListener.onDataChangeListener(planetList);
                }
                if (filteredPeoples.size() > 0)
                    peoplesAdapter.addAll(filteredPeoples);

                if (peoples.size() >= 10) {
                    peoplesAdapter.addFooter();
                    loadMoreItems();
                } else {
                    isLastPage = true;
                }
            }
        }

        @Override
        public void onFailure(Call<PeopleResponse> call, Throwable t) {
            NetworkLogUtility.logFailure(call, t);

            if (!call.isCanceled()) {
                if (NetworkUtility.isKnownException(t)) {
                    peoplesAdapter.updateFooter(PeopleAdapter.FooterType.ERROR);
                }
            }
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        type = bundle.getString("type");

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        peoplesAdapter = new PeopleAdapter();
        peoplesAdapter.setOnReloadClickListener(this);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(peoplesAdapter);

        // Pagination
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        Call call = apiService.getPeopleData(String.valueOf(currentPage));
        call.enqueue(findVideosFirstFetchCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeListeners();
        currentPage = 1;
        unbinder.unbind();
    }

    @OnClick(R.id.reload_btn)
    public void onReloadButtonClicked() {
        errorLinearLayout.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.VISIBLE);

        Call findVideosCall = apiService.getPeopleData(String.valueOf(currentPage));
        findVideosCall.enqueue(findVideosFirstFetchCallback);
    }

    @Override
    public void onReloadClick() {
        peoplesAdapter.updateFooter(PeopleAdapter.FooterType.LOAD_MORE);

        Call findLikedVideosCall = apiService.getPeopleData(String.valueOf(currentPage));
        findLikedVideosCall.enqueue(findVideosNextFetchCallback);
    }

    private void loadMoreItems() {
        isLoading = true;

        currentPage += 1;


        Call call = apiService.getPeopleData(String.valueOf(currentPage));
        call.enqueue(findVideosNextFetchCallback);
    }

    private void removeListeners() {
        recyclerView.removeOnScrollListener(recyclerViewOnScrollListener);
    }

}