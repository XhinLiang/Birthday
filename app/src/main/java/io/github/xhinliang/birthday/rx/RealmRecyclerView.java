
package io.github.xhinliang.birthday.rx;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmRecyclerView {

    public static abstract class ListAdapter<T extends RealmObject, VH extends ViewHolder> extends RecyclerView.Adapter<VH> {

        protected final LayoutInflater inflater;
        protected final RealmResults<T> data;
        private final AtomicInteger refs = new AtomicInteger();

        private final RealmChangeListener listener = new RealmChangeListener() {
            @Override
            public void onChange() {
                notifyDataSetChanged();
            }
        };

        public ListAdapter(Context context, RealmResults<T> data) {
            this.inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            if (refs.getAndIncrement() == 0) {
                data.addChangeListener(listener);
            }
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            if (refs.decrementAndGet() == 0) {
                data.removeChangeListener(listener);
            }
        }
    }

    public static class ViewHolder<V extends ViewDataBinding> extends RecyclerView.ViewHolder {

        public final V binding;

        public ViewHolder(V binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
