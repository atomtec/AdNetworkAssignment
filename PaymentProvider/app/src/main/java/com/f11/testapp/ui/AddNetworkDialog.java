package com.f11.testapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.f11.testapp.data.AdProvider;
import com.f11.testapp.R;
import com.f11.testapp.databinding.InputDialogBinding;

import java.util.Objects;

public class AddNetworkDialog extends DialogFragment {

    private InputDialogBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = InputDialogBinding.inflate(inflater, container, false);

        Objects.requireNonNull(getDialog()).setTitle(R.string.add_ad_network);

        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle cancel button click
                dismiss();
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateEditTexts()) {
                    return;
                }

                Bundle result = new Bundle();
                result.putParcelable("adProvider", createPaymentProviderFromInputs());

                getParentFragmentManager().setFragmentResult("saveClicked", result);

                dismiss();
            }
        });

        return binding.getRoot();
    }

    private boolean validateEditTexts() {
        boolean isValid = true;

        // Check if any of the EditText fields are empty
        if (TextUtils.isEmpty(Objects.requireNonNull(binding.aliasEditText.getText()).toString().trim())) {
            binding.aliasEditText.setError(getString(R.string.alias_cannot_be_blank));
            isValid = false;
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(binding.nameEditText.getText()).toString().trim())) {
            binding.nameEditText.setError(getString(R.string.name_cannot_be_blank));
            isValid = false;
        }

        if (TextUtils.isEmpty(binding.privacyUrlEditText.getText().toString().trim())) {
            binding.privacyUrlEditText.setError(getString(R.string.privacy_url_cannot_be_blank));
            isValid = false;
        }

        return isValid;
    }

    private AdProvider createPaymentProviderFromInputs() {
        String alias = binding.aliasEditText.getText().toString();
        String name = binding.nameEditText.getText().toString();
        String privacyUrl = binding.privacyUrlEditText.getText().toString();
        AdProvider paymentProvider = new AdProvider(0, alias, name, privacyUrl);
        return paymentProvider;
    }

}