package mobile.apac.com.apac;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ApacFragment extends Fragment {

    /** The CardView widget. */
    //@VisibleForTesting
    CardView mCardView;

    TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_apac,container,false);

        mCardView = (CardView) view.findViewById(R.id.cardview);
        mTextView = (TextView) view.findViewById(R.id.about_content);
        // About APAC text
        String text = "<html><body>"
                + "<p align='justify'>"
                + getString(R.string.apac_about_content)
                + "</p> "
                + "</body></html>";
        Spanned sp = Html.fromHtml(text);
        mTextView.setText(sp);

        Log.e("ApacFragment", "ApacFragment Called");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Your codes also goes here.
        Log.e("ApacFragment", "ApacFragment Resumed");
    }
}
