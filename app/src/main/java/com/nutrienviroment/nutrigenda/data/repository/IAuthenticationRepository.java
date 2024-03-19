package com.nutrienviroment.nutrigenda.data.repository;

import com.google.firebase.auth.FirebaseUser;

public interface IAuthenticationRepository {
    void register(String email, String password, AuthListener listener);

    interface AuthListener {
        void onSuccess(FirebaseUser user);
        void onError(String error);
    }

}
