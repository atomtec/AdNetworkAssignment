package com.f11.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.f11.testapp.data.AdProvider;
import com.f11.testapp.databinding.ActivityMainBinding;
import com.f11.testapp.ui.AddNetworkDialog;
import com.f11.testapp.ui.ListAdProviderDialogFragment;
import com.f11.testapp.viewmodel.AdViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AdViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(AdViewModel.class);

        getSupportFragmentManager().setFragmentResultListener("saveClicked",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        // Extract the AdProvider object from the bundle
                        AdProvider adProvider = bundle.getParcelable("adProvider");

                        if (adProvider != null) {
                            viewModel.insertAdProvider(adProvider).observe(MainActivity.this, result -> {
                                if (result.isSuccess()) {
                                    // Handle successful insertion
                                    showToast(getString(R.string.adprovider_inserted_successfully));
                                } else {
                                    // Handle error case
                                    String errorMessage = result.getErrorMessage();
                                    showToast(errorMessage);
                                }
                            });
                        } else {
                            showToast(getString(R.string.adprovider_object_is_null));
                        }
                    }
                });
        getSupportFragmentManager().setFragmentResultListener("updateClicked",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        // Extract the AdProvider object from the bundle
                        AdProvider adProvider = bundle.getParcelable("adProvider");

                        if (adProvider != null) {
                            viewModel.updateAdProvider(adProvider).observe(MainActivity.this, result -> {
                                if (result.isSuccess()) {
                                    // Handle successful update
                                    showToast(getString(R.string.adprovider_updated_successfully));
                                } else {
                                    // Handle error case
                                    String errorMessage = result.getErrorMessage();
                                    showToast(errorMessage);
                                }
                            });
                        } else {
                            showToast(getString(R.string.adprovider_object_is_null));
                        }
                    }
                });
        getSupportFragmentManager().setFragmentResultListener("deleteClicked",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        // Extract the AdProvider object from the bundle
                        AdProvider adProvider = bundle.getParcelable("adProvider");

                        if (adProvider != null) {
                            viewModel.deleteAdProvider(adProvider).observe(MainActivity.this, result -> {
                                if (result.isSuccess()) {
                                    // Handle successful deletion
                                    showToast(getString(R.string.adprovider_deleted_successfully));
                                } else {
                                    // Handle error case
                                    String errorMessage = result.getErrorMessage();
                                    showToast(errorMessage);
                                }
                            });
                        } else {
                            showToast(getString(R.string.adprovider_object_is_null));
                        }
                    }
                });
        // Set click listener for the save button
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the dialog when save button is clicked
                AddNetworkDialog dialog = new AddNetworkDialog();
                dialog.show(getSupportFragmentManager(), "AddNetworkDialog");
            }
        });

        binding.viewButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewModel.getAllProviders();

            }
        });
        viewModel.allProvidersLiveData.observe(MainActivity.this, adProviders -> {
            if (adProviders != null && !adProviders.isEmpty()) {
                ListAdProviderDialogFragment dialog = ListAdProviderDialogFragment.newInstance(adProviders);
                dialog.show(getSupportFragmentManager(), "ListAdProviderDialogFragment");
            } else {
                showToast(getString(R.string.no_ad_providers_saved));
            }
        });
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static List<AdProvider> getDummyAdProviders() {
        List<AdProvider> adProviders = new ArrayList<>();
        adProviders.add(new AdProvider(1, "ad_network_1", "Ad Network One", "https://www.adnetwork1.com/privacy"));
        adProviders.add(new AdProvider(2, "ad_network_2", "Ad Network Two", "https://www.adnetwork2.com/privacy"));
        adProviders.add(new AdProvider(3, "ad_network_3", "Ad Network Three", "https://www.adnetwork3.com/privacy"));
        adProviders.add(new AdProvider(4, "ad_network_4", "Ad Network Four", "https://www.adnetwork4.com/privacy"));
        return adProviders;
    }



}