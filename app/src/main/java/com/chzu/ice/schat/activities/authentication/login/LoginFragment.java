package com.chzu.ice.schat.activities.authentication.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chzu.ice.schat.R;
import com.chzu.ice.schat.activities.authentication.register.RegisterFragment;
import com.chzu.ice.schat.activities.main.MainActivity;

import java.util.Objects;

public class LoginFragment extends Fragment {
    private Button signInBtn;
    private TextView toSignUpBtn;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_frag_login, container, false);
        signInBtn = view.findViewById(R.id.signInBtn);
        toSignUpBtn = view.findViewById(R.id.toSignUpBtn);
        signInBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        });
        toSignUpBtn.setOnClickListener((v) -> {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.replace(R.id.authMainContent, RegisterFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
}
