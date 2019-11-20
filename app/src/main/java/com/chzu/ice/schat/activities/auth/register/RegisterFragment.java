package com.chzu.ice.schat.activities.auth.register;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chzu.ice.schat.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class RegisterFragment extends Fragment implements RegisterContract.View {
    private RegisterContract.Presenter presenter;
    private ImageView toSignInBtn;
    private Button signUpBtn;
    private EditText usernameEdt;
    private EditText passwordEdt;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                goBack();
                return true;
            }
            return false;
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_frag_register, container, false);
        new ImplRegisterPresenter(this);
        toSignInBtn = view.findViewById(R.id.toSignInBtn);
        signUpBtn = view.findViewById(R.id.signUpBtn);
        usernameEdt = view.findViewById(R.id.usernameEdt);
        passwordEdt = view.findViewById(R.id.passwordEdt);

        usernameEdt.requestFocus();

        toSignInBtn.setOnClickListener((v -> goBack()));

        signUpBtn.setOnClickListener((v) -> {
            String username = usernameEdt.getText().toString().trim();
            String password = passwordEdt.getText().toString().trim();
            presenter.register(username, password);
        });
        return view;
    }

    private void goBack() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction;
        if (fragmentManager != null) {
            transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragmentManager.popBackStack();
            transaction.commit();
        }
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void afterRegister() {
        signUpBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_auth_background_blue));
        signUpBtn.setText("SIGN UP...");
    }

    @Override
    public void endRegister() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            signUpBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_auth_background_black));
            signUpBtn.setText("SIGN UP");
        });
    }

    @Override
    public void showRegisterFailedForUsernameExist() {
        new Handler(Looper.getMainLooper()).post(() -> {
                    View view = getActivity().findViewById(R.id.authMainContent);
                    Snackbar snackbar = Snackbar.make(view, "用户已存在!", Snackbar.LENGTH_SHORT);
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
    public void showRegisterSucceed() {
        new Handler(Looper.getMainLooper()).post(() -> {
                    View view = getActivity().findViewById(R.id.authMainContent);
                    Snackbar snackbar = Snackbar.make(view, "注册成功！", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(Color.GREEN);
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
                        public void onAnimationStart(Animator animation) {
                            snackbar.show();
                            super.onAnimationStart(animation);
                        }
                    });
                    valueAnimator.start();
                }
        );
    }
}
