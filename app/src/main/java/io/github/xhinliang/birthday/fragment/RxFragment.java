package io.github.xhinliang.birthday.fragment;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.xhinliang.lib.fragment.BaseFragment;

public abstract class RxFragment<V extends ViewDataBinding> extends BaseFragment {

    protected V binding;

    @Nullable
    public abstract V onCreateBinding(LayoutInflater inflater, @Nullable ViewGroup container,
                                      @Nullable Bundle savedInstanceState);

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        binding = onCreateBinding(inflater, container, savedInstanceState);
        return binding == null ? null : binding.getRoot();
    }


}
