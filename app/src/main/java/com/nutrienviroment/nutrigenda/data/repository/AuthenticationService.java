package com.nutrienviroment.nutrigenda.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nutrienviroment.nutrigenda.data.repository.IAuthenticationRepository;

public class AuthenticationService implements IAuthenticationRepository {

    private FirebaseAuth firebaseAuth;

    public AuthenticationService() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void register(String email, String password, AuthListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        listener.onSuccess(user);
                    } else {
                        listener.onError(task.getException().getMessage());
                    }
                });
    }
}
