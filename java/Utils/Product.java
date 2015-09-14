package Utils;

/**
 * Created by guillaumeagis on 21/04/15.
 * Product object, contain information related to the venue
 */
public class Product {

    // short name
    private String _title;

    //desc
    private String _price;
    // url img
    private String _image;

    private int _imageDrawable;

    public Product(final String title,final String image,  final String price)
    {
        this._title = title;
        this._price = price;
        this._image = image;
    }

    public Product(final String title,final int imageDrawable)
    {
        this._title = title;
        this._imageDrawable = imageDrawable;
    }


    public String getTitle() {
        return _title;
    }

    public String getPrice() {
        return _price;
    }


    public String getImage() {
        return _image;
    }

    public int getDrawable()
    {
        return _imageDrawable;
    }


}
