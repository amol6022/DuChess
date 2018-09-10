import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

/**
 * Created by Amol on 8/14/2018.
 */

public class PieceAdapter extends ArrayAdapter<ImageClass> {
    public PieceAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);

    }


}
