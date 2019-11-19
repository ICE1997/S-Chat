package com.chzu.ice.schat.activities.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chzu.ice.schat.R;
import com.chzu.ice.schat.activities.auth.register.RegisterFragment;
import com.chzu.ice.schat.activities.main.MainActivity;

import java.util.Objects;

public class LoginFragment extends Fragment implements LoginContract.View{
    private Button signInBtn;
    private TextView toSignUpBtn;
    private EditText usernameEdt;
    private EditText passwordEdt;
    private LoginContract.Presenter presenter;

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
        new ImplLoginPresenter(this);
        signInBtn = view.findViewById(R.id.signInBtn);
        toSignUpBtn = view.findViewById(R.id.toSignUpBtn);
        usernameEdt = view.findViewById(R.id.usernameEdt);
        passwordEdt = view.findViewById(R.id.passwordEdt);

        signInBtn.setOnClickListener((v) -> {
            String username = usernameEdt.getText().toString().trim();
            String password = usernameEdt.getText().toString().trim();
            presenter.login(username,password);

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

    @Override
    public void showLoginFailedForWrongUsernameOrPassword() {

    }

    @Override
    public void showLoginSucceed() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
