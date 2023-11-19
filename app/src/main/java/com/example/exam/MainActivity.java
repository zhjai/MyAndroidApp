package com.example.exam;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exam.data.DataBank;
import com.example.exam.data.ShopItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> addItemLauncher;
    ActivityResultLauncher<Intent> updateItemLauncher;
    private ArrayList<ShopItem> shopItems = new ArrayList<>();
    private ShopItemAdapter shopItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mainRecyclerView = findViewById(R.id.recyclerview_main);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        shopItems = new DataBank().loadShopItems(MainActivity.this);

        if (shopItems.size() == 0) {
            shopItems.add(new ShopItem("青椒", 1.5, R.drawable.qingjiao));
        }

        shopItemAdapter = new ShopItemAdapter(shopItems);
        mainRecyclerView.setAdapter(shopItemAdapter);

        registerForContextMenu(mainRecyclerView);

        addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String name = data.getStringExtra("name");
                        String priceText = data.getStringExtra("price");

                        double price = Double.parseDouble(priceText);
                        shopItems.add(new ShopItem(name, price, R.drawable.baicai));
                        shopItemAdapter.notifyItemInserted(shopItems.size());

                        new DataBank().SaveShopItems(MainActivity.this, shopItems);

                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {}
                }
        );

        updateItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int position = data.getIntExtra("position", 0);
                        String name = data.getStringExtra("name");
                        String priceText = data.getStringExtra("price");

                        double price = Double.parseDouble(priceText);
                        ShopItem shopItem = shopItems.get(position);
                        shopItem.setName(name);
                        shopItem.setPrice(price);

                        shopItemAdapter.notifyItemChanged(position);

                        new DataBank().SaveShopItems(MainActivity.this, shopItems);

                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {}
                }
        );
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent intentAdd = new Intent(this, ShopItemDetailsActivity.class);
                addItemLauncher.launch(intentAdd);
                break;
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Data");
                builder.setMessage("Are you sure to delete this data?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    shopItems.remove(item.getOrder());
                    shopItemAdapter.notifyItemRemoved(item.getOrder());
                    new DataBank().SaveShopItems(MainActivity.this, shopItems);
                });
                builder.setNegativeButton("No", (dialog, which) -> {});
                builder.show();
                break;
            case 2:
                Intent intentUpdate = new Intent(this, ShopItemDetailsActivity.class);
                ShopItem shopItem = shopItems.get(item.getOrder());
                intentUpdate.putExtra("name", shopItem.getName());
                intentUpdate.putExtra("price", shopItem.getPrice());
                intentUpdate.putExtra("position", item.getOrder());
                updateItemLauncher.launch(intentUpdate);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    public static class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder> {
        private ArrayList<ShopItem> shopItemArrayList;

        public ShopItemAdapter(ArrayList<ShopItem> shopItems) {
            shopItemArrayList = shopItems;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.shop_item_row, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.getTextViewName().setText(shopItemArrayList.get(position).getName());
            viewHolder.getTextViewPrice().setText(shopItemArrayList.get(position).getPrice()+"");
            viewHolder.getImageViewItem().setImageResource(shopItemArrayList.get(position).getImageResourceId());
        }

        @Override
        public int getItemCount() {
            return shopItemArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textViewName;
            private final TextView textViewPrice;
            private final ImageView imageViewItem;

            public ViewHolder(View shopItemView) {
                super(shopItemView);

                textViewName = shopItemView.findViewById(R.id.textview_item_name);
                textViewPrice = shopItemView.findViewById(R.id.textview_item_price);
                imageViewItem = shopItemView.findViewById(R.id.imageview_item);
                shopItemView.setOnCreateContextMenuListener(this);
            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.setHeaderTitle("具体操作");

                contextMenu.add(0, 0, this.getAdapterPosition(), "添加"+this.getAdapterPosition());
                contextMenu.add(0, 1, this.getAdapterPosition(), "删除"+this.getAdapterPosition());
                contextMenu.add(0, 2, this.getAdapterPosition(), "修改"+this.getAdapterPosition());
            }

            public TextView getTextViewName() {
                return textViewName;
            }

            public TextView getTextViewPrice() {
                return textViewPrice;
            }

            public ImageView getImageViewItem() {
                return imageViewItem;
            }
        }
    }
}