package com.swat_cat.com.e_adds.Controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swat_cat.com.e_adds.Entities.Category;
import com.swat_cat.com.e_adds.R;

import java.util.ArrayList;

/**
 * Created by Dell on 11.06.2015.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewHolder> {

    private ArrayList<Category> categories;
    private Context context;

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;
        private TextView categoryDescription;


        public ItemViewHolder(View itemView) {
            super(itemView);
            this.categoryName=(TextView)itemView.findViewById(R.id.L_textView);
            this.categoryDescription=(TextView)itemView.findViewById(R.id.M_textView);
        }

    }

    public CategoryAdapter(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycle_view_item,parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryName.setText(category.getName());
        holder.categoryDescription.setText(category.getDescription());
    }

    @Override
    public int getItemCount() {
        return null!=categories?categories.size():0;
    }
}
