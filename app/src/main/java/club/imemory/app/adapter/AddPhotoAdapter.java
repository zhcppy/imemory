package club.imemory.app.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import club.imemory.app.R;
import club.imemory.app.activity.CreateLifeActivity;
import club.imemory.app.util.AppManager;

/**
 * @Author: 张杭
 * @Date: 2017/3/23 20:41
 */

public class AddPhotoAdapter extends RecyclerView.Adapter<AddPhotoAdapter.ViewHolder> {

    private Context mContext;

    private List<String> mList;

    public AddPhotoAdapter(List<String> list) {
        mList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            imageView = (ImageView) itemView.findViewById(R.id.image_photo);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String path = mList.get(position);
                if (mList.size()-1==position){
                    AppManager.showToast("选择图片");
                    CreateLifeActivity activity = (CreateLifeActivity) mContext;
                    activity.openAlbum();
                }else{
                    AppManager.showToast(path);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String path = mList.get(position);
        if (mList.size()-1==position){
            Glide.with(mContext).load(Integer.valueOf(path))
                    .fitCenter()
                    .thumbnail(0.1f)
                    .into(holder.imageView);
        }else{
            Glide.with(mContext).load(path)
                    .thumbnail(0.1f)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
