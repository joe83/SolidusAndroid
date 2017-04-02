package com.scanba.solidusandroid.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.scanba.solidusandroid.R;
import com.scanba.solidusandroid.adapters.CartItemsAdapter;
import com.scanba.solidusandroid.api.ApiClient;
import com.scanba.solidusandroid.api.SolidusInterface;
import com.scanba.solidusandroid.components.CustomProgressDialog;
import com.scanba.solidusandroid.components.ListItemDecorator;
import com.scanba.solidusandroid.models.CartItem;
import com.scanba.solidusandroid.models.Product;
import com.scanba.solidusandroid.models.containers.ProductsContainer;
import com.scanba.solidusandroid.sqlite.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<CartItem> mCartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initUI();
        fetchProducts();
    }

    private void initUI() {
        mRecyclerView = (RecyclerView) findViewById(R.id.cart_items);
    }

    private void fetchProducts() {
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        try {
            Dao<CartItem, Integer> cartItemDao = databaseHelper.getCartItemDao();
            mCartItems = cartItemDao.queryForAll();
            if(mCartItems.size() > 0) {
                String ids = "";
                for(CartItem cartItem : mCartItems) {
                    ids += cartItem.productId + ",";
                }
                final CustomProgressDialog dialog = new CustomProgressDialog(this, "Loading...");
                dialog.show();
                SolidusInterface apiService = ApiClient.getClient().create(SolidusInterface.class);
                Call<ProductsContainer> container = apiService.getProducts(1, ids, ApiClient.API_KEY);
                container.enqueue(new Callback<ProductsContainer>() {
                    @Override
                    public void onResponse(Call<ProductsContainer> call, Response<ProductsContainer> response) {
                        List<Product> products = response.body().getProducts();
                        setupCart(products);
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ProductsContainer> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setupCart(List<Product> products) {
        ListIterator<CartItem> itr = mCartItems.listIterator();
        CartItem cartItem;
        int productsCount;
        while(itr.hasNext()) {
            productsCount = 0;
            for(Product product : products) {
                cartItem = itr.next();
                if(product.getId() == cartItem.productId) {
                    cartItem.name = product.getName();
                    break;
                }
                productsCount++;
            }
            if(productsCount == products.size())
                itr.remove();
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CartItemsAdapter adapter = new CartItemsAdapter(this, mCartItems);
        mRecyclerView.setAdapter(adapter);

    }
}