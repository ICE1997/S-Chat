package com.chzu.ice.schat.activities.authentication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chzu.ice.schat.R;
import com.chzu.ice.schat.activities.authentication.login.LoginFragment;
import com.chzu.ice.schat.utils.UIManager;

public class AuthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIManager.setFullScreen(getWindow());
        setContentView(R.layout.auth_act);

        FragmentManager fragmentManager = getSupportFragmentManager();

        LoginFragment loginFragment = (LoginFragment) fragmentManager.findFragmentById(R.id.authMainContent);

        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.authMainContent, loginFragment);
            transaction.commit();
        }
    }
}
