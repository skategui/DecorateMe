package Utils;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.spleat.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by guillaumeagis on 21/04/15.
 * Picasso load the image from an url into a given image
 *
 */
public class ImageUtils {

    /**
     * Load image from an url into a ImageView.
     * Encode url as well
     * If the image cannot be load (because of the size for example), try to load it smaller
     * @param image imageView to put the image in
     * @param activity current activity
     */
    public static void loadImage(int drawable, final ImageView image, final Activity activity)
    {
        image.setBackground(activity.getResources().getDrawable(R.drawable.logo));
        if (drawable != 0)
        Picasso.with(activity).load(drawable).into(image);
    }

    public static void loadImageUrl(String url, final ImageView image, final Activity activity)
    {
        image.setBackground(activity.getResources().getDrawable(R.drawable.logo));
        if ("".equals(url) == false && url != null)
            Picasso.with(activity).load(url).into(image);
    }
}
