
package com.pybeta.ui.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Class that stores references to children of a view that get updated when the
 * item in the view changes
 */
public abstract class ItemView {

    /**
     * Create item view storing references to children of given view to be
     * accessed when the view is ready to display an item
     *
     * @param view
     */
    public ItemView(final View view) {
        // Intentionally left blank
    }

    /**
     * Get text view with id
     *
     * @param view
     * @param id
     * @return text view
     */
    protected TextView textView(final View view, final int id) {
        return (TextView) view.findViewById(id);
    }

    /**
     * Get image view with id
     *
     * @param view
     * @param id
     * @return text view
     */
    protected ImageView imageView(final View view, final int id) {
        return (ImageView) view.findViewById(id);
    }
}
