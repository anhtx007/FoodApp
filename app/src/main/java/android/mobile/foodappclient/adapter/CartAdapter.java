package android.mobile.foodappclient.adapter;

import android.content.Context;
import android.mobile.foodappclient.R;
import android.mobile.foodappclient.model.CartItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewHolder> {
    List<CartItem> list;
    Context context;
    public double totalAmount = 0;

    public CartAdapter(List<CartItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {


        CartItem item = list.get(position);
        holder.nameCart.setText(list.get(position).getProductname());
        holder.priceCart.setText(String.valueOf(list.get(position).getPrice()));
        holder.numCart.setText(String.valueOf(list.get(position).getQuantity()));
        double totalItem = item.getPrice() * item.getQuantity();
        holder.totalCart.setText(String.valueOf(totalItem));
        totalAmount += totalItem;
        holder.cboCart.setChecked(item.isChecked());
        holder.cboCart.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            item.setChecked(isChecked);
            list.set(holder.getAdapterPosition(), item); // Cập nhật trạng thái của mục trong danh sách
            notifyDataSetChanged(); // Cập nhật giao diện
        }));

        holder.minCart.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                holder.numCart.setText(String.valueOf(item.getQuantity()));

                double newTotal = item.getPrice() * item.getQuantity();
                holder.totalCart.setText(String.valueOf(newTotal));

                totalAmount -= item.getPrice();
            } else if (item.getQuantity() == 1) {
                // Giảm số lượng về 0 và ẩn sản phẩm
                item.setQuantity(0);
                holder.numCart.setText(String.valueOf(0));
                holder.totalCart.setText(String.valueOf(0));
                holder.itemView.setVisibility(View.GONE);
                totalAmount -= item.getPrice();
            }

        });

        String imageUrl = "http://192.168.102.8:3000"+item.getImageproduct();
        Glide.with(context)
                .load(imageUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(50))) // Bo tròn với bán kính 50 pixel
                .into(holder.img_picCart);

        holder.maxCart.setOnClickListener(v -> {

            item.setQuantity(item.getQuantity() + 1);
            holder.numCart.setText(String.valueOf(item.getQuantity()));
            double newTotal = item.getPrice() * item.getQuantity();
            holder.totalCart.setText(String.valueOf(newTotal));
            Toast.makeText(context, "total"+newTotal, Toast.LENGTH_SHORT).show();
            totalAmount += item.getPrice();

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public List<CartItem> getSelectedItems() {
        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : list) {
            if (item.isChecked()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView img_picCart;
        TextView minCart, maxCart, numCart, totalCart, priceCart, nameCart;
        CheckBox cboCart;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img_picCart = itemView.findViewById(R.id.img_pic_cart);
            minCart = itemView.findViewById(R.id.min_btn_cart);
            maxCart = itemView.findViewById(R.id.plus_btn_cart);
            numCart = itemView.findViewById(R.id.txt_num_cart);
            totalCart = itemView.findViewById(R.id.txt_toTalCart);
            priceCart = itemView.findViewById(R.id.txt_priceCart);
            nameCart = itemView.findViewById(R.id.txt_title_cart);
            cboCart = itemView.findViewById(R.id.checkbox);


        }
    }
}
