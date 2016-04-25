package Custom_views;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Philipp on 10/04/2015.
 */
public class Ship extends ImageView implements Parcelable {

    public ArrayList<Integer> OccupiedSqaures;

    public Ship(Context context) {
        super(context);
        OccupiedSqaures = new ArrayList<>();
    }

    public Ship(Context context, AttributeSet attrs) {
        super(context, attrs);
        OccupiedSqaures = new ArrayList<>();
    }

    public Ship(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        OccupiedSqaures = new ArrayList<>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
