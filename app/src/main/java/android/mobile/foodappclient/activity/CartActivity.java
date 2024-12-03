package android.mobile.foodappclient.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.mobile.foodappclient.adpater.CartAdapter;
import android.mobile.foodappclient.databinding.ActivityCartBinding;
import android.mobile.foodappclient.model.Cart;
import android.mobile.foodappclient.model.CartItem;
import android.mobile.foodappclient.service.ApiService;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    private double Total = 0;
    private int itemCount = 0;
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonOrder.setOnClickListener(v -> {
            List<CartItem> selectedItems = adapter.getSelectedItems();// bên adapter
            Intent intent = new Intent(CartActivity.this, PlaceOrderActivity.class);
            intent.putExtra("cartItemList", (Serializable) selectedItems);
            //không cần truyền itemlist nữa vì đã lấy được danh sách từ các mục ở adapter
            intent.putExtra("totalAmount",Total); // Sử dụng tổng tiền đã được cập nhật trong Adapter
            startActivity(intent);
        });

        getCart();
    }
    private void getCart() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPre", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "-1");
        Log.d("ID nayf5555", "onResponse: " + userId);

        Cart cart = new Cart();
        cart.setUserId(userId);
        ApiService.api.getCart(cart).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful()) {

                    List<CartItem> cartItemList = response.body();
                    adapter = new CartAdapter(cartItemList);
                    itemCount = cartItemList.size();
                    Log.d("count", "onResponse: " +itemCount);

                    binding.recyclerViewCart.setAdapter(adapter);
                    binding.recyclerViewCart.setLayoutManager(new LinearLayoutManager(CartActivity.this, RecyclerView.VERTICAL, false));
                    Total = caculateTotal(cartItemList);
                    binding.toTalCart.setText(String.valueOf(Total));
                    Toast.makeText(CartActivity.this, "Call OK", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CartActivity.this, "Lỗi" + response.errorBody(), Toast.LENGTH_SHORT).show();
                    Log.d("dot", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("dot", "onResponse: " + t.getMessage());
            }
        });

    }
    private double caculateTotal(List<CartItem> cartItemList){

        for (CartItem item: cartItemList){
            Log.d("t", "caculateTotal: "+item.getTotal_order());
            Total += item.getTotal_order();
            Log.d("to", "caculateTotal: "+Total);
        }
        return Total;
    }
}