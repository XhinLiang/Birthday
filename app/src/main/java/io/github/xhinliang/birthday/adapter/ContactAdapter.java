package io.github.xhinliang.birthday.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.databinding.RecyclerItemContactBinding;
import io.github.xhinliang.birthday.model.Contact;
import io.github.xhinliang.birthday.rx.RealmRecyclerView;
import io.github.xhinliang.birthday.util.ImageUtils;
import io.realm.RealmResults;

public class ContactAdapter extends RealmRecyclerView.ListAdapter<Contact, ContactAdapter.ViewHolder> {

    private Context context;
    private Listener listener;

    public ContactAdapter(Context context, RealmResults<Contact> data, Listener listener) {
        super(context, data);
        this.context = context;
        this.listener = listener;
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
        if (!TextUtils.isEmpty(item.getPicture())) {
            String imagePath = String.format("%s/%s", context.getFilesDir().getAbsolutePath(), item.getPicture());
            holder.binding.ivAvatar.setImageBitmap(ImageUtils.compressImageByPixel(imagePath, 200));
        } else
            holder.binding.ivAvatar.setImageResource(R.drawable.ic_account_box_black_24dp);
        holder.itemView.setTag(item);
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

    public class ViewHolder extends RealmRecyclerView.ViewHolder<RecyclerItemContactBinding> {

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
