package android.mobile.foodappclient.adpater;

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

public class BestfoodAdapter extends RecyclerView.Adapter<BestfoodAdapter.viewHolder> {
    List<Product> list_food;
    Context context;

    public BestfoodAdapter(List<Product> list_food) {
        this.list_food = list_food;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_best_food,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Product product = list_food.get(position);

        holder.txt_productName.setText(list_food.get(position).getProductname());
        holder.txt_price.setText(String.valueOf(list_food.get(position).getPrice()+""+"đ"));

        String imageUrl = "http://192.168.1.191:3000/"+product.getImageproduct();
        Log.d("ImageURL", "Full URL: " + imageUrl);


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
                .into(holder.img_food);

        holder.itemView.setOnClickListener(v -> {
//            String productId = li.get(position).getId();

            Intent intent = new Intent(context, DetailActivity.class);
            Log.d("product111111","678"+product.toString());
            intent.putExtra("productId",product);

            context.startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {
        return list_food.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txt_productName,txt_description,txt_price;
        ImageView img_food;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txt_productName = itemView.findViewById(R.id.txt_productName);
//            txt_description = itemView.findViewById(R.id.txt_description);
            txt_price = itemView.findViewById(R.id.txt_price);
            img_food = itemView.findViewById(R.id.img_food);

        }
    }
}
