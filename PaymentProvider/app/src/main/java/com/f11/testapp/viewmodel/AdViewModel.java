package com.f11.testapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.f11.testapp.DataOperationResult;
import com.f11.testapp.data.AdProvider;
import com.f11.testapp.data.AdProviderDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AdViewModel extends ViewModel {


    private  AdProviderDao adProviderDao;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4); // Adjust thread count as needed


    private MutableLiveData<List<AdProvider>> resultLiveData = new MutableLiveData<>();

    public LiveData<List<AdProvider>> allProvidersLiveData = resultLiveData;

    @Inject
    public AdViewModel(AdProviderDao adProviderDao) {
        this.adProviderDao = adProviderDao;
    }


    // Method to insert a new AdProvider (using custom thread executor)
    public LiveData<DataOperationResult> insertAdProvider(AdProvider provider) {
        MutableLiveData<DataOperationResult> resultLiveData = new MutableLiveData<>();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    adProviderDao.insert(provider);
                    resultLiveData.postValue(new DataOperationResult(true, null));
                } catch (Exception e) {
                    resultLiveData.postValue(new DataOperationResult(false, e.getMessage()));
                }
            }
        });
        return resultLiveData;
    }

    // Method to update an existing AdProvider
    public LiveData<DataOperationResult> updateAdProvider(AdProvider provider) {
        MutableLiveData<DataOperationResult> resultLiveData = new MutableLiveData<>();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    adProviderDao.update(provider);
                    resultLiveData.postValue(new DataOperationResult(true, null));
                } catch (Exception e) {
                    resultLiveData.postValue(new DataOperationResult(false, e.getMessage()));
                }
            }
        });
        return resultLiveData;
    }

    // Method to delete an AdProvider
    public LiveData<DataOperationResult> deleteAdProvider(AdProvider provider) {
        MutableLiveData<DataOperationResult> resultLiveData = new MutableLiveData<>();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    adProviderDao.delete(provider);
                    resultLiveData.postValue(new DataOperationResult(true, null));
                } catch (Exception e) {
                    resultLiveData.postValue(new DataOperationResult(false, e.getMessage()));
                }
            }
        });
        return resultLiveData;
    }

    // Method to get a LiveData list of all AdProviders sorted by name
    public void getAllProviders() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                List<AdProvider> providers = adProviderDao.getAllProviders();
                resultLiveData.postValue(providers);
            }
        });
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown(); // Shutdown the executor when ViewModel is cleared
    }
}

