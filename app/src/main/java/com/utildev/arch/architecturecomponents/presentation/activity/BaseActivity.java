package com.utildev.arch.architecturecomponents.presentation.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.utildev.arch.architecturecomponents.R;
import com.utildev.arch.architecturecomponents.common.utilities.MySharedPreferences;
import com.utildev.arch.architecturecomponents.di.MyApplication;
import com.utildev.arch.architecturecomponents.presentation.fragment.BaseFragment;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {
    @Inject
    protected MySharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.appComponent.inject(this);
    }

    private void transactionFragment(BaseFragment fragment, boolean replace, boolean addToBackStack, boolean animation) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm != null && fragment != null) {
            FragmentTransaction transaction = fm.beginTransaction();
            if (animation) {
                transaction.setCustomAnimations(R.anim.slide_enter_from_right, R.anim.slide_exit_to_left,
                        R.anim.slide_enter_from_left, R.anim.slide_exit_to_right);
            } else {
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            if (replace) {
                transaction.replace(R.id.flContainer, fragment, fragment.getClass().getSimpleName());
            } else {
                BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.flContainer);
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                transaction.add(R.id.flContainer, fragment, fragment.getClass().getSimpleName());
            }
            if (addToBackStack) {
                transaction.addToBackStack(fragment.getClass().getSimpleName());
            }
            transaction.commit();
        }
    }

    public void replaceFragment(BaseFragment fragment, boolean addToBackStack, boolean animation) {
        transactionFragment(fragment, true, addToBackStack, animation);
    }

    public void addFragment(BaseFragment fragment, boolean addToBackStack, boolean animation) {
        transactionFragment(fragment, false, addToBackStack, animation);
    }

    public void clearAllStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int fmCount = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < fmCount; i++) {
            fragmentManager.popBackStack();
        }
    }

    public void clearStack() {
        getSupportFragmentManager().popBackStack();
    }
}
