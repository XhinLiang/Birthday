package io.github.xhinliang.birthday.adapter;

import android.content.Context;
import android.databinding.ObservableList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.databinding.RecyclerItemContactBinding;
import io.github.xhinliang.birthday.model.Contact;
import io.github.xhinliang.birthday.reactivex.BindingRecyclerView;
import io.github.xhinliang.birthday.util.ImageUtils;

public class ContactAdapter extends BindingRecyclerView.ListAdapter<Contact, ContactAdapter.ViewHolder> {

    private Listener listener;
    private RequestManager requestManager;

    public ContactAdapter(Context context, ObservableList<Contact> data, Listener listener, RequestManager requestManager) {
        super(context, data);
        this.listener = listener;
        this.requestManager = requestManager;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(RecyclerItemContactBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact item = data.get(position);
        holder.binding.setContact(item);
        // execute the binding immediately to ensure
        // the original size of RatioImageView is set before layout
        holder.binding.executePendingBindings();
        holder.binding.ivAvatar.setImageBitmap(ImageUtils.compressImageByPixel(item.getPicture(), 200));
        setupImage(holder.binding.ivAvatar, null);
        holder.itemView.setTag(item);
    }

    private void setupImage(ImageView image, String imageUrl) {
        if (imageUrl == null || imageUrl.length() == 0) {
            image.setImageResource(R.drawable.ic_account_box_black_24dp);
            return;
        }
        requestManager.load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_error_black_24dp)
                .into(image);
    }

    //重载这个方法并设置 RecyclerView#setHasStableIds能大幅度提高性能
    @Override
    public long getItemId(int position) {
        return data.get(position).getName().hashCode();
    }

    //一种在ViewHolder里实现点击事件的方法,绑定当前Adapter的Activity实现这个接口即可
    public interface Listener {
        void onUserItemClick(ViewHolder holder);
    }

    public class ViewHolder extends BindingRecyclerView.ViewHolder<RecyclerItemContactBinding> {

        public ViewHolder(RecyclerItemContactBinding binding) {
            super(binding);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserItemClick(ViewHolder.this);
                }
            });
        }
    }
}
