package com.chzu.ice.schat.activities.auth.register;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chzu.ice.schat.R;

import java.util.Objects;

public class RegisterFragment extends Fragment {
    private ImageView toSignInBtn;

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
        toSignInBtn = view.findViewById(R.id.toSignInBtn);
        toSignInBtn.setOnClickListener((v -> {
            goBack();
        }));
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
}
