package com.nutrienviroment.nutrigenda.ui.authentication.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.nutrienviroment.nutrigenda.data.repository.IAuthenticationRepository;

public class RegisterViewModel extends ViewModel implements IAuthenticationRepository.AuthListener {
    private final IAuthenticationRepository repository;
    private final MutableLiveData<Boolean> _registrationSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> _registrationError = new MutableLiveData<>();

    public LiveData<Boolean> registrationSuccess = _registrationSuccess;
    public LiveData<String> registrationError = _registrationError;

    public RegisterViewModel(IAuthenticationRepository repository) {
        this.repository = repository;
    }

    public void register(String email, String password) {
        repository.register(email, password, this);
    }

    @Override
    public void onSuccess(FirebaseUser user) {
        _registrationSuccess.postValue(true);
    }

    @Override
    public void onError(String error) {
        _registrationError.postValue(error);
    }
}
