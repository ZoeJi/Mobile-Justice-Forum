package mobile.apac.com.apac;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyAccountFragment extends Fragment {
    private String[] data = { "My questions", "My answers", "My followings", "Logout"};
    private ImageView accountImage;
    private TextView username;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_account,container,false);
        accountImage = (ImageView) rootView.findViewById(R.id.imageView);
        username = (TextView) rootView.findViewById(R.id.username);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, data);
        final ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemposition = position;
                String value = (String) listView.getItemAtPosition(position);
                Intent myIntent;
                switch (value){
                    case "My questions":
                        myIntent = new Intent(getActivity(), MyQuestionsActivity.class);
                        getActivity().startActivity(myIntent);
                        break;
                    case "My answers":
                        myIntent = new Intent(getActivity(), MyAnswersActivity.class);
                        getActivity().startActivity(myIntent);
                        break;
                    case "My followings":
                        myIntent = new Intent(getActivity(), MyFollowingsActivity.class);
                        getActivity().startActivity(myIntent);
                        break;
                    case "Logout":
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("Logout");

                        dialog.setMessage("Are you sure to logout?");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                        break;
                    default:
                        break;
                }

            }
        });

            return rootView;
        }


    @Override
    public void onResume() {
        super.onResume();

        // Your codes also goes here.
        Log.e("MyAccountFragment", "MyAccountFragment Resumed");
    }
}
