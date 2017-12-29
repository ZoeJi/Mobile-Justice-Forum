package mobile.apac.com.apac.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import mobile.apac.com.apac.R;
import mobile.apac.com.apac.schema.Forum;

/**
 * Created by haklim on 11/16/15.
 */
public class MainForumAdapter extends ArrayAdapter<Forum> {

    private final Context mContext;
    private ArrayList<Forum> mForumArryList;

    public MainForumAdapter(Context context, ArrayList<Forum> items){
        super(context, 0,items);
        mForumArryList = items;
        this.mContext = context;
    }

    public void setItemsArrayList(ArrayList<Forum> forumArrayList){
        this.mForumArryList = forumArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // If we weren't given a view, inflate one.
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.generic_list_item, null);
        }

        String updateStringContent_title = getItem(position).getTitle();

        TextView titleTextview = (TextView) convertView.findViewById(R.id.genericLayoutTextView);
        titleTextview.setText(updateStringContent_title);

//        TextView descTextView = (TextView) convertView.findViewById(R.id.contentDesc);
//        descTextView.setText(getItem(position).getDescription());

        TextView dateTextView = (TextView) convertView.findViewById(R.id.date);
        Date date = new Date();
        date.setTime(getItem(position).getLastModified().getTime());
        dateTextView.setText(date.toString());

//        int bottomColor = Color.parseColor("#99FF99");
//        int topColor = Color.parseColor("#CCFFCC");
//
//        GradientDrawable gradientDrawable = new GradientDrawable(
//                GradientDrawable.Orientation.BOTTOM_TOP,
//                new int[] {bottomColor, topColor});
//        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
//        gradientDrawable.setCornerRadius(10.f);
//
//        convertView.setBackground(gradientDrawable);

        return convertView;
    }

    @Override
    public Forum getItem(int position) {
        return mForumArryList.get(position);
    }

    @Override
    public int getCount() {
        return mForumArryList.size();
    }


}
