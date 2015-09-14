package recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * Created by guillaumeagis on 21/04/15.
 * Recycler view adapter, with the list of venues
 */
public class RecyclerViewAdapterTrolley extends RecyclerView.Adapter<RecyclerViewAdapterTrolley.ViewHolder>{


    private ArrayList<ItemTrolleyCard> _objectsList = new ArrayList<>();
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View v)
        {
            super(v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapterTrolley(ArrayList<ItemTrolleyCard> objectsList)
    {
       _objectsList = objectsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterTrolley.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ItemTrolleyCard.itemAdapter(ItemTrolleyCard.getView(parent));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ItemTrolleyCard vo = _objectsList.get(position);
        vo.updateView(holder);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return _objectsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
