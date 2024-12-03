package android.mobile.foodappclient.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.mobile.foodappclient.databinding.ActivityDetailBinding;
import android.mobile.foodappclient.model.Cart;
import android.mobile.foodappclient.model.CartResponse;
import android.mobile.foodappclient.model.WithList;
import android.mobile.foodappclient.service.ApiService;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.mobile.foodappclient.R;
import android.mobile.foodappclient.model.Product;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailActivity extends AppCompatActivity {
    private int quantity = 1;

    ActivityDetailBinding binding;
    public boolean Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Product product = (Product) getIntent().getSerializableExtra("productId");
        binding.txtProductnameDetail.setText(product.getProductname());
        binding.txtPriceDetail.setText(String.valueOf(product.getPrice()));
        binding.txtDescriptionDetail.setText(product.getDescription());
        binding.txtTotal.setText(String.valueOf(product.getPrice()));


        String imgUrl = "http://192.168.0.101:3000/" + product.getImageproduct();
        Glide.with(this)
                .load(imgUrl)
                .into(binding.picDetail);

        binding.btnFavourite.setOnClickListener(v -> {
            if (!Image) {
                binding.btnFavourite.setImageResource(R.drawable.favourite_red02);
                Image = true;
            } else {
                binding.btnFavourite.setImageResource(R.drawable.favourite_white02);
                Image = false;
            }
            addToWithList();
        });
        binding.imgBackDetail.setOnClickListener(v -> {
            finish();
        });

        binding.plusBtn.setOnClickListener(v -> {
            quantity = quantity + 1;
            binding.txtNumTotal.setText(quantity + "");
            binding.txtTotal.setText(String.valueOf(quantity * product.getPrice()));
        });

        binding.minBtn.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity = quantity - 1;
                binding.txtNumTotal.setText(quantity + "");
                binding.txtTotal.setText(String.valueOf(quantity * product.getPrice()));
            }
        });
        binding.btnBuyNow.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, BuyActivity.class);
            intent.putExtra("productId", product); // product là đối tượng Product bạn muốn truyền đi
            intent.putExtra("productname", product.getProductname());
            intent.putExtra("total", binding.txtTotal.getText());
            intent.putExtra("quantity", quantity);
            Log.d("Tag", "total" + binding.txtTotal.getText());
            intent.putExtra("price", product.getPrice());// quantity là số lượng sản phẩm
            startActivity(intent);

        });


        binding.btnAddtocart.setOnClickListener(v -> {
            addToCart();
        });
    }

    // Phương thức để tìm ID tài nguyên ảnh dựa trên chuỗi ID
    private void addToWithList() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPre", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "-1");
        Log.d("taList", "withList: " + userId);

        Product product = (Product) getIntent().getSerializableExtra("productId");
        String productId = product.getId();

        WithList withList = new WithList(userId, productId);

        ApiService.api.addToWithList(withList).enqueue(new Callback<WithList>() {
            @Override
            public void onResponse(Call<WithList> call, Response<WithList> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailActivity.this, "Đã thêm sản phẩm vào danh sách yêu thích của bạn", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(DetailActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WithList> call, Throwable t) {
                Log.d("Lỗi", "Lỗi : " + t.getMessage());
                Toast.makeText(DetailActivity.this, "Đã có lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCart() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPre", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "-1");
        Log.d("ta", "addToCart: " + userId);

        Product product = (Product) getIntent().getSerializableExtra("productId");
        String productId = product.getId();
        int num = Integer.parseInt(binding.txtNumTotal.getText().toString());
        double price = Double.valueOf((binding.txtPriceDetail.getText().toString()));
        double total_order = Double.valueOf((binding.txtTotal.getText().toString()));

        Log.d("pId", "pId :" + productId);
        Log.d("num", "num :" + num);
        Log.d("price", "price :" + price);
        Log.d("total", "total :" + total_order);

        Cart cart = new Cart(userId, productId, price, total_order, num);

        ApiService.api.addToCart(cart).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {

                if (response.isSuccessful()) {
                    CartResponse cartResponse = (CartResponse) response.body();

                    Toast.makeText(DetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, "Lỗi" + response.errorBody(), Toast.LENGTH_SHORT).show();
                    Log.d("dot", "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Log.d("Tag", "onFailure: " + t.getMessage());
            }
        });
    }
}