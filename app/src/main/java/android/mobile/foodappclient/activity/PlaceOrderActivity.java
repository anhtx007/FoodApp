package android.mobile.foodappclient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.mobile.foodappclient.R;
import android.mobile.foodappclient.adpater.PlaceOrderAdapter;
import android.mobile.foodappclient.databinding.ActivityPlaceOrderBinding;
import android.mobile.foodappclient.model.Address;
import android.mobile.foodappclient.model.CartItem;
import android.mobile.foodappclient.model.ItemOrder;
import android.mobile.foodappclient.model.Order;
import android.mobile.foodappclient.service.ApiService;
import android.mobile.foodappclient.service.OrderService;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderActivity extends AppCompatActivity {
    ActivityPlaceOrderBinding binding;
    PlaceOrderAdapter adapter;

    ArrayAdapter<String> spinnerAdapter;
    String[] paymentMethod = {"Tiền mặt", "Thẻ tín dụng", "MoMo"};
    private String selectedAddress;
    private String methodPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getAddressUser();

        binding.btnHuyBuyPlace.setOnClickListener(v -> {
            finish();
        });
        binding.btnBuyNowPlace.setOnClickListener(v -> {
            PlaceOrder();
        });
        binding.btnAddtoAddressPlace.setOnClickListener(v -> {
            addToAddress();
        });


        List<CartItem> cartItemList = (List<CartItem>) getIntent().getSerializableExtra("cartItemList");
        Log.d("VCL", "onCreate: " + cartItemList);
//        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0);
//        binding.totalPlace.setText(String.valueOf(totalAmount));
        double totalAmount = 0;

// Tính toán lại tổng tiền dựa trên danh sách các mặt hàng được chọn
        for (CartItem item : cartItemList) {
            //tính tổng số tiền của mỗi mặt hàng rồi nhân với sô lượng tương ứng và cộng vào biến totalAmount
            totalAmount += item.getPrice() * item.getQuantity();
        }

// Đặt lại tổng tiền cho giao diện
        binding.totalPlace.setText(String.valueOf(totalAmount));

        adapter = new PlaceOrderAdapter(cartItemList);
        binding.recyclerViewPlaceOrder.setAdapter(adapter);
        binding.recyclerViewPlaceOrder.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        spinnerAdapter = new ArrayAdapter<>(this, R.layout.sp_item, paymentMethod);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPaymentPlace.setAdapter(spinnerAdapter);

        binding.spinnerPaymentPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ///xử lí click chọn thanh toán
                methodPay = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerLocationPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ///xử lí click chọn địa chỉ
                selectedAddress = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getAddressUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPre", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "-1");
        Log.d("ID nè", "ở Address " + userId);
        Address address = new Address();
        address.setUserId(userId);
        ApiService.api.getAddress(address).enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if (response.isSuccessful()) {

                    Address a = response.body();
                    Log.d("Address2", "onResponse: " + a.getAddress());
//                    List<String> addressList = a.getAddress(); // tạo list để gán dữ liệu lên adapter
                    List<String> addressList = new ArrayList<>();
                    for (String address : a.getAddress()) {
                        if (address != null) {
                            addressList.add(address);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(PlaceOrderActivity.this, R.layout.sp_item, addressList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerLocationPlace.setAdapter(adapter);
                    Toast.makeText(PlaceOrderActivity.this, "OK", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PlaceOrderActivity.this, "OK" + response.errorBody(), Toast.LENGTH_SHORT).show();
                    Log.d("ERROR2222223333", "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                Toast.makeText(PlaceOrderActivity.this, "OK" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ERROR33333", "onResponse: " + t.getMessage());
            }
        });
    }

    private void PlaceOrder() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPre", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "-1");
        SharedPreferences sharedPreferences_name = this.getSharedPreferences("myPre", MODE_PRIVATE);
        String userName = sharedPreferences_name.getString("userName", "");
        Intent intent = getIntent();


        List<CartItem> cartItemList = (List<CartItem>) getIntent().getSerializableExtra("cartItemList");
        Log.d("VCL", "onCreate: " + cartItemList);
//        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0);
//        Log.d("totalAmount", "onCreate: " + totalAmount);
        double totalAmount = 0;

// Tính toán lại tổng tiền dựa trên danh sách các mặt hàng được chọn
        for (CartItem item : cartItemList) {
            totalAmount += item.getPrice() * item.getQuantity();
        }

        List<ItemOrder> itemOrders = new ArrayList<>();

        for (CartItem ca : cartItemList) {
            String productname = ca.getProductname();
            Log.d("productname", "PlaceOrder: " + productname);
            int quantity = ca.getQuantity();
            Log.d("quantity", "PlaceOrder: " + quantity);
            double price = ca.getPrice();
            Log.d("price", "PlaceOrder: " + price);

            ItemOrder itemOrder = new ItemOrder();
            itemOrder.setProductname(productname);
            itemOrder.setQuantity(quantity);
            itemOrder.setPrice(price);
            itemOrders.add(itemOrder);

        }


        //
        Order order = new Order();
        order.setUsername(userName);
        order.setTotal(Double.parseDouble(String.valueOf(totalAmount)));
        order.setAddress(selectedAddress);
        order.setItemOrders(itemOrders);
        order.setPayment_method(methodPay);

        OrderService.api.addOrder(order).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    Order orderRes = response.body();
                    String msg = orderRes.getMsg();
                    Log.d("helo", "onResponse: " + msg);
                    showOrderSuccessDialog();

                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(PlaceOrderActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addToAddress() {
        TextInputEditText ed_address;
        Button btn_add_address;

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.bottomsheet_buy, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PlaceOrderActivity.this);
        bottomSheetDialog.setContentView(view);

        ed_address = view.findViewById(R.id.edt_diachi);
        btn_add_address = view.findViewById(R.id.btn_address);
        btn_add_address.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("myPre", MODE_PRIVATE);
            String userId = sharedPreferences.getString("userId", "-1");
            String address = ed_address.getText().toString();

            Log.d("TAG", "addToAddress: " + userId);

            ApiService.api.addAddress(userId, address).enqueue(new Callback<Address>() {
                @Override
                public void onResponse(Call<Address> call, Response<Address> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PlaceOrderActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        getAddressUser();
                        bottomSheetDialog.dismiss(); // Tắt BottomSheetDialog sau khi thêm thành công
                    } else {
                        Toast.makeText(PlaceOrderActivity.this, "Lỗi" + response.errorBody(), Toast.LENGTH_SHORT).show();
                        Log.d("e", "onResponse: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<Address> call, Throwable t) {
                    Log.d("TAG", "onFailure: " + t.toString());
                    getAddressUser();
                    bottomSheetDialog.dismiss();
                }
            });
        });

        bottomSheetDialog.show();

    }

    private void showOrderSuccessDialog() {

        // Hiển thị dialog thông báo đặt hàng thành công
        AlertDialog.Builder builder = new AlertDialog.Builder(PlaceOrderActivity.this);
        builder.setTitle("Đặt hàng thành công");
        builder.setMessage("Cảm ơn bạn đã đặt hàng!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng dialog
                dialog.dismiss();
                // Truyền dữ liệu cần hiển thị
                // Chuyển đến hoạt động hoặc màn hình khác tùy theo nhu cầu của bạn
                startActivity(new Intent(PlaceOrderActivity.this, MainActivity.class));

            }
        });
        builder.setCancelable(false); // Không cho phép hủy dialog bằng cách nhấn nút back
        builder.show();
    }
}