package android.mobile.foodappclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.mobile.foodappclient.R;
import android.mobile.foodappclient.activity.DetailActivity;
import android.mobile.foodappclient.model.Product;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.viewHolder> {
    List<Product> productList;
    Context context;

    public FavoriteAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Product product = productList.get(position);
        String imageUrl = "http://192.168.1.191:3000/" + product.getImageproduct();
//http://192.168.0.100:3000

        Glide.with(context)
                .load(imageUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(50))) // Bo tròn với bán kính 50 pixel
                .into(holder.pic_list);

        holder.txt_nameList.setText(productList.get(position).getProductname());
        holder.txt_priceList.setText(String.valueOf(productList.get(position).getPrice()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("productId", product);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView pic_list;
        TextView txt_nameList, txt_priceList;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            pic_list = itemView.findViewById(R.id.pic_list);
            txt_nameList = itemView.findViewById(R.id.txt_nameList);
            txt_priceList = itemView.findViewById(R.id.txt_priceList);
        }
    }
}
