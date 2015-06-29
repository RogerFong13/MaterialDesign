package com.wanda.fangke.instagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanda.fangke.instagram.Beans.GlobalMenuItem;
import com.wanda.fangke.instagram.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fangke on 2015/6/15.
 */
public class GlobalMenuAdapter extends ArrayAdapter<GlobalMenuItem> {

    private static final int TYPE_MENU_ITEM = 0;
    private static final int TYPE_DIVIDER = 1;

    private final LayoutInflater mInflater;
    private final List<GlobalMenuItem> menuItems = new ArrayList<GlobalMenuItem>();

    public GlobalMenuAdapter(Context context) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
        setupMenuItems();
    }

    private void setupMenuItems(){

        menuItems.add(new GlobalMenuItem(R.mipmap.ic_global_menu_feed,"My Feed"));
        menuItems.add(new GlobalMenuItem(R.mipmap.ic_global_menu_direct, "Instagram Direct"));
        menuItems.add(new GlobalMenuItem(R.mipmap.ic_global_menu_news, "News"));
        menuItems.add(new GlobalMenuItem(R.mipmap.ic_global_menu_popular, "Popular"));
        menuItems.add(new GlobalMenuItem(R.mipmap.ic_global_menu_nearby, "Photos Nearby"));
        menuItems.add(new GlobalMenuItem(R.mipmap.ic_global_menu_likes, "Photos You've Liked"));
        menuItems.add(GlobalMenuItem.dividerMenuItem());
        menuItems.add(new GlobalMenuItem(0,"Settings"));
        menuItems.add(new GlobalMenuItem(0,"About"));
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public GlobalMenuItem getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return menuItems.get(position).isDivider?TYPE_DIVIDER:TYPE_MENU_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(getItemViewType(position) == TYPE_MENU_ITEM){
            MenuItemViewHolder  holder;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.item_global_menu,parent,false);
                holder = new MenuItemViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (MenuItemViewHolder) convertView.getTag();
            }

            GlobalMenuItem item = getItem(position);
            holder.tvLabel.setText(item.label);
            holder.ivIcon.setImageResource(item.iconResId);
            holder.ivIcon.setVisibility(item.iconResId == 0?View.GONE:View.VISIBLE);
            return  convertView;
        }else{
            return mInflater.inflate(R.layout.item_menu_divider, parent, false);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return !menuItems.get(position).isDivider;
    }

    public static class MenuItemViewHolder {
        @InjectView(R.id.ivIcon)
        ImageView ivIcon;
        @InjectView(R.id.tvLabel)
        TextView tvLabel;

        public MenuItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
