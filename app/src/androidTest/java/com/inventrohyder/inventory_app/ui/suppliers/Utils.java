package com.inventrohyder.inventory_app.ui.suppliers;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.internal.util.Checks;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

class Utils {
    public static Matcher<RecyclerView.ViewHolder> withSupplierId(final int supplierId) {
        Checks.checkNotNull(supplierId);
        return new BoundedMatcher<RecyclerView.ViewHolder, SuppliersRecyclerAdapter.ViewHolder>(SuppliersRecyclerAdapter.ViewHolder.class) {
            @Override
            protected boolean matchesSafely(SuppliersRecyclerAdapter.ViewHolder holder) {
                boolean isMatches = false;
                if (holder != null) {
                    isMatches = supplierId == holder.mId;
                }
                return isMatches;
            }


            @Override
            public void describeTo(Description description) {
                description.appendText("with supplier id: " + supplierId);
            }
        };
    }
}
