package android.mobile.foodappclient.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import android.mobile.foodappclient.adpater.FavoriteAdapter;
import android.mobile.foodappclient.model.Product;
import android.mobile.foodappclient.model.WithList;
import android.mobile.foodappclient.service.ApiService;
import android.mobile.foodappclient.databinding.FragmentNotificationBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationFragment extends Fragment {
    FragmentNotificationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        getWithList();
        return binding.getRoot();
    }

    private void getWithList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPre", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "-1");
        Log.d("ID nayf5555", "onResponse: " + userId);

        WithList withList = new WithList();
        withList.setUserId(userId);

        ApiService.api.getFavorite(withList).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> productList = response.body();
                    for (Product pro : productList) {
                        Log.d("WithList", "ccccc :" + pro.getId() + pro.getProductname() + pro.getDescription() + pro.getPrice() + pro.getImageproduct());

                    }
                    FavoriteAdapter adapter = new FavoriteAdapter(productList);
                    binding.recyeFavorite.setAdapter(adapter);
                    binding.recyeFavorite.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                } else {
                    String errorBody = response.errorBody().toString();
                    Log.d("ERRO", "E :" + errorBody);
                    Toast.makeText(getContext(), "Hiển thị thất bại" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


}