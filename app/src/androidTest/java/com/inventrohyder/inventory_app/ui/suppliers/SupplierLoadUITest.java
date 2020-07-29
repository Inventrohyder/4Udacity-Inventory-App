package com.inventrohyder.inventory_app.ui.suppliers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.inventrohyder.inventory_app.InputTestUtils;
import com.inventrohyder.inventory_app.MainActivity;
import com.inventrohyder.inventory_app.R;
import com.inventrohyder.inventory_app.data.InventoryProviderContract.Suppliers;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.inventrohyder.inventory_app.ui.suppliers.Utils.withSupplierId;

@RunWith(AndroidJUnit4.class)
public class SupplierLoadUITest {

    private static int supplier_1_id;
    private static int supplier_2_id;
    private static ContentValues values_1;
    private static ContentValues values_2;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void insertSuppliers() {
        ContentResolver contentResolver = ApplicationProvider.getApplicationContext().getContentResolver();


        values_1 = new ContentValues();
        values_1.put(Suppliers.COLUMN_NAME, InputTestUtils.Supplier_1.name);
        values_1.put(Suppliers.COLUMN_SUPPLIER_EMAIL, InputTestUtils.Supplier_1.email);
        values_1.put(Suppliers.COLUMN_SUPPLIER_PHONE, InputTestUtils.Supplier_1.phone);

        Uri insertedSupplier_1_Uri = contentResolver.insert(Suppliers.CONTENT_URI, values_1);
        assert insertedSupplier_1_Uri != null;
        supplier_1_id = (int) ContentUris.parseId(insertedSupplier_1_Uri);


        values_2 = new ContentValues();
        values_2.put(Suppliers.COLUMN_NAME, InputTestUtils.Supplier_2.name);
        values_2.put(Suppliers.COLUMN_SUPPLIER_EMAIL, InputTestUtils.Supplier_2.email);
        values_2.put(Suppliers.COLUMN_SUPPLIER_PHONE, InputTestUtils.Supplier_2.phone);

        Uri insertedSupplier_2_Uri = contentResolver.insert(Suppliers.CONTENT_URI, values_2);
        assert insertedSupplier_2_Uri != null;
        supplier_2_id = (int) ContentUris.parseId(insertedSupplier_2_Uri);

    }

    @Test
    public void testCheckSupplierIsShown() {
        // Move to the suppliers tab
        onView(ViewMatchers.withId(R.id.navigation_suppliers)).perform(click());

        // Move to the view holder with the first id
        onView(withId(R.id.suppliers_list))
                .perform(
                        RecyclerViewActions.scrollToHolder(
                                withSupplierId(supplier_1_id)
                        )
                )
                .check(
                        matches(
                                hasDescendant(withText(values_1.getAsString(Suppliers.COLUMN_NAME)))
                        )
                );

        // Move to the view holder with the second id
        onView(withId(R.id.suppliers_list))
                .perform(
                        RecyclerViewActions.scrollToHolder(
                                withSupplierId(supplier_2_id)
                        )
                )
                .check(
                        matches(
                                hasDescendant(withText(values_2.getAsString(Suppliers.COLUMN_NAME)))
                        )
                );

    }

}