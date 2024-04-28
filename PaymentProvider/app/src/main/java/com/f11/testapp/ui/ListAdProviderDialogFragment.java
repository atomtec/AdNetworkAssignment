package com.f11.testapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.f11.testapp.data.AdProvider;
import com.f11.testapp.adapter.AdViewExpandableListAdapter;
import com.f11.testapp.R;
import com.f11.testapp.databinding.DialogAdProviderBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListAdProviderDialogFragment extends DialogFragment  implements ExpandableListView.OnGroupClickListener  {
    private List<AdProvider> adProviderList;

    private DialogAdProviderBinding binding;

    public static ListAdProviderDialogFragment newInstance(List<AdProvider> adProviderList) {
        ListAdProviderDialogFragment fragment = new ListAdProviderDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("adProviderList", new ArrayList<>(adProviderList));
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adProviderList = getArguments().getParcelableArrayList("adProviderList");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)  {
        binding = DialogAdProviderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        Objects.requireNonNull(getDialog()).setTitle(R.string.ad_network);
        AdViewExpandableListAdapter adapter = new AdViewExpandableListAdapter(
                requireContext(),
                adProviderList,
                tobeDeletedRow -> {
                    Bundle result = new Bundle();
                    result.putParcelable("adProvider", tobeDeletedRow);

                    getParentFragmentManager().setFragmentResult("deleteClicked", result);

                    dismiss();
                },
                toBeUpdatedRow -> {
                    Bundle result = new Bundle();
                    result.putParcelable("adProvider", toBeUpdatedRow);

                    getParentFragmentManager().setFragmentResult("updateClicked", result);

                    dismiss();

                }

        );
        binding.expandableListview.setAdapter(adapter);
        binding.expandableListview.setOnGroupClickListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
        if (!binding.expandableListview.isGroupExpanded(i)) {
            expandableListView.expandGroup(i);
        } else {
            expandableListView.collapseGroup(i);
        }
        return true; // Consume the click event
    }
}
