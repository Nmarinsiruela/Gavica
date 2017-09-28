package nmarin.appgenerators.com.gavica.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nmarin.appgenerators.com.gavica.R;

public class JourneyListAdapter extends BaseAdapter {
    private final Context mContext;
       private final ArrayList<JourneyHeader> mNavItems;

    public JourneyListAdapter(Context context, ArrayList<JourneyHeader> navItems) {
        mContext = context;
        mNavItems = navItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.journey_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = view.findViewById(R.id.titleJourney);
        TextView startView = view.findViewById(R.id.startJourney);
        TextView returnView = view.findViewById(R.id.endJourney);

        titleView.setText( mNavItems.get(position).mTitle );
        startView.setText( mNavItems.get(position).mdateStart );
        returnView.setText( mNavItems.get(position).mdateReturn );

//        Typeface typefacebtn = Typeface.createFromAsset(mContext.getAssets(), "fonts/Actor-Regular.ttf");
//        titleView.setTypeface(typefacebtn);
//        subtitleView.setTypeface(typefacebtn);
//        titleView.setTextSize(20);


        return view;
    }
}