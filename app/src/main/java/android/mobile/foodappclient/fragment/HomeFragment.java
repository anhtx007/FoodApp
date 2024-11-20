package android.mobile.foodappclient.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.mobile.foodappclient.R;
import android.mobile.foodappclient.model.Product;
import android.mobile.foodappclient.service.ProductService;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;

    PhotoAdapter adapter;
    android.mobile.foodappclient.adpater.BestfoodAdapter foodAdapter;
    CategoryfoodAdapter categoryfoodAdapter;

    private ArrayList<Photo> photoList;
    //    private ArrayList<Food> foodList;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Kiểm tra ảnh khi không còn ảnh nào để kiểm tra
            if (binding.viewPager.getCurrentItem() == photoList.size() - 1) {
                binding.viewPager.setCurrentItem(0); // Quay trở lại ảnh ban đầu
            } else {
                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1); // Chuyển sang 1 ảnh tiếp theo
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        SharedPreferences sharedPreferences_name = getActivity().getSharedPreferences("myPre", MODE_PRIVATE);
        String userName = sharedPreferences_name.getString("userName", ""); // Lấy tên người dùng từ SharedPreferences

        // Gán tên người dùng vào TextView hoặc phần tử giao diện tương ứng
        binding.txtNameClient.setText(userName);


        photoList = new ArrayList<>();
        photoList.add(new Photo(R.drawable.piza));
        photoList.add(new Photo(R.drawable.burger));
        photoList.add(new Photo(R.drawable.hotdog));
        photoList.add(new Photo(R.drawable.kimchi));


        adapter = new PhotoAdapter(getContext(), photoList);
        binding.viewPager.setAdapter(adapter);
        binding.circleCenter.setViewPager(binding.viewPager);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL);
//        binding.recyclerViewBestfood.addItemDecoration(dividerItemDecoration);
//        foodList = new ArrayList<>();

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 3000);
            }
        });
        getBestfood();
        getCategory();
        getSearchfood();

        binding.logOut.setOnClickListener(v -> {
            getDialogOut();
        });

        binding.cartBtn.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CartActivity.class));
        });
        return binding.getRoot();
    }

    private void getSearchfood() {
        binding.btnSearch.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), SearchActivity.class));
        });
    }

    private void getBestfood() {
        ProductService.api.getSanPham().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> list_product = response.body();
                    for (Product product : list_product) {
                        Log.d("Image", "ảnh đây: "+product.getImageproduct());
                        Log.d("Product", "ProductName" + product.getId() + product.getProductname() + product.getPrice()+product.getImageproduct());
                    }

//                    Toast.makeText(getContext(), "Call API thành công", Toast.LENGTH_SHORT).show();
                    foodAdapter = new android.mobile.foodappclient.adpater.BestfoodAdapter(list_product);
                    binding.recyclerViewBestfood.setAdapter(foodAdapter);
                    binding.recyclerViewBestfood.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                } else {
                    Log.e("API_CALL_FAILURE1", "Không thể nhận dữ liệu sản phẩm từ API: " + response.message());
                    Toast.makeText(getContext(), "Không thể nhận dữ liệu sản phẩm từ API: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_CALL_FAILURE", "Thất bại thật rồi: " + t.getMessage());
                Toast.makeText(getContext(), "Call Api thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

