package ru.symdeveloper.SeGALLERY;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

public class SegalAdapter extends BaseAdapter {

    private static final String LOG_TAG = "SegalAdapter";

    private Context context;
    private SegalEntity.SegalEntitiesList items;

    public SegalAdapter(Context context, SegalEntity.SegalEntitiesList items) {
        super();
        this.items = items;
        this.context = context;
    }

    public void setItems(SegalEntity.SegalEntitiesList items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup root) {
        SegalViewHolder holder;
        SegalEntity item = items.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_segal, null);
            holder = new SegalViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SegalViewHolder) convertView.getTag();
        }
        holder.bindView(item);
        return convertView;
    }

    class SegalViewHolder {
        private ImageView imageView;

        public SegalViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.segal_image);
        }

        public void bindView(SegalEntity object) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(object.getWidth(), object.getHeight());
            imageView.setLayoutParams(layoutParams);
            imageView.setImageBitmap(null);
            ImageLoader.getInstance().displayImage(object.getImageUrl(), imageView);
        }
    }

}
