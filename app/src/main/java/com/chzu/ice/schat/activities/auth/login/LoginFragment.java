package com.chzu.ice.schat.activities.auth.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chzu.ice.schat.App;
import com.chzu.ice.schat.R;
import com.chzu.ice.schat.activities.auth.register.RegisterFragment;
import com.chzu.ice.schat.activities.main.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class LoginFragment extends Fragment implements LoginContract.View {
    private static final String TAG = LoginFragment.class.getSimpleName();
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

        usernameEdt.requestFocus();


        signInBtn.setOnClickListener((v) -> {
            String username = usernameEdt.getText().toString().trim();
            String password = passwordEdt.getText().toString().trim();
            presenter.login(username, password);
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
        new Handler(Looper.getMainLooper()).post(() -> {
                    View view = getActivity().findViewById(R.id.authMainContent);
                    Snackbar snackbar = Snackbar.make(view, "密码或用户名错误!", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(Color.RED);
                    ViewGroup.LayoutParams layoutParams = snackbar.getView().getLayoutParams();
                    ValueAnimator valueAnimator = new ValueAnimator();
                    valueAnimator.setDuration(300);
                    valueAnimator.setIntValues(0, 148);
                    valueAnimator.addUpdateListener(animation -> {
                        layoutParams.height = (int) valueAnimator.getAnimatedValue();
                        snackbar.getView().setLayoutParams(layoutParams);
                        snackbar.getView().requestLayout();
                    });
                    valueAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            usernameEdt.requestFocus();
                            super.onAnimationEnd(animation);
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
                            snackbar.show();
                            super.onAnimationStart(animation);
                        }
                    });
                    valueAnimator.start();
                }
        );
    }

    @Override
    public void showLoginSucceed() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }

    @Override
    public void afterLogin() {
        signInBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_auth_background_blue));
        signInBtn.setText("SIGN IN...");
    }

    @Override
    public void endLogin() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            signInBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_auth_background_black));
            signInBtn.setText("SIGN IN");
        });
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
