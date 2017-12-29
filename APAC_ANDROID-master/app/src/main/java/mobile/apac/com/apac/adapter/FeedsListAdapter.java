package mobile.apac.com.apac.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import mobile.apac.com.apac.R;
import mobile.apac.com.apac.schema.Feed;


/**
 * Created by haklim on 11/16/15.
 */
public class FeedsListAdapter extends ArrayAdapter {

    private final Context mContext;
    private ArrayList<Feed> mForumArryList;

    public FeedsListAdapter(Context context, ArrayList<Feed> items){
        super(context, 0,items);
        this.mForumArryList = items;
        this.mContext = context;
    }

    public void setItemsArrayList(ArrayList<Feed> forumArrayList){
        this.mForumArryList = forumArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // If we weren't given a view, inflate one.
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.generic_list_item_alist, null);
        }

        String updateStringContent = getItem(position).getContent();

        TextView answersTextview = (TextView) convertView.findViewById(R.id.genericContent);
        answersTextview.setText(updateStringContent);

//        TextView descTextView = (TextView) convertView.findViewById(R.id.contentDesc);
//        descTextView.setText(getItem(position).getDescription());

        TextView dateTextView = (TextView) convertView.findViewById(R.id.date);
        Date date = new Date();
        date.setTime(getItem(position).getPostedDate().getTime());
        dateTextView.setText(date.toString());

        return convertView;
    }

    @Override
    public Feed getItem(int position) {
        return mForumArryList.get(position);
    }

    @Override
    public int getCount() {
        return mForumArryList.size();
    }

    @Override
    public long getItemId(int position) {
        return mForumArryList.get(position).getFeedId();
    }
}
