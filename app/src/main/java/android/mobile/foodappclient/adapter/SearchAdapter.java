package android.mobile.foodappclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import android.mobile.foodappclient.activity.DetailActivity;
import android.mobile.foodappclient.model.Product;
import android.mobile.foodappclient.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewHolder> {
    List<Product> list;
    Context context;

    public SearchAdapter(List<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listfood,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Product product = list.get(position);
        String imageUrl = "http://192.168.0.101:3000/"+product.getImageproduct();


        Glide.with(context)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        if (e != null) {
                            Log.e("Glide3333", "Failed to load image: " + e.getMessage(), e);
                        } else {
                            Log.e("Glide4444", "Unknown error occurred while loading image");
                        }
                        return false;

                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.img_picSearch);
        holder.txt_titleSearch.setText(list.get(position).getProductname());
        holder.priceSearch.setText(String.valueOf(list.get(position).getPrice()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("productId",product);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView img_picSearch;
        TextView txt_titleSearch,priceSearch;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img_picSearch = itemView.findViewById(R.id.img_listFood);
            txt_titleSearch = itemView.findViewById(R.id.title_txt);
            priceSearch = itemView.findViewById(R.id.price_txt);
        }
    }
}
