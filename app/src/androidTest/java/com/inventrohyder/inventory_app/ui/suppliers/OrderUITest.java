package com.inventrohyder.inventory_app.ui.suppliers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;

import com.inventrohyder.inventory_app.InputTestUtils;
import com.inventrohyder.inventory_app.MainActivity;
import com.inventrohyder.inventory_app.R;
import com.inventrohyder.inventory_app.data.InventoryProviderContract.Suppliers;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.inventrohyder.inventory_app.ui.suppliers.Utils.clickChildViewWithId;
import static com.inventrohyder.inventory_app.ui.suppliers.Utils.withSupplierId;
import static org.hamcrest.Matchers.allOf;

public class OrderUITest {
    private static ContentValues values_withoutPhone;
    private static int supplier_withoutPhone_id;
    private static int supplier_withoutEmail_id;
    private static Context context;
    private static ContentValues values_withoutEmail;
    private static int supplierId;
    @Rule
    public IntentsTestRule<MainActivity> mMainActivityActivityTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @BeforeClass
    public static void insertSuppliers() {
        context = ApplicationProvider.getApplicationContext();
        ContentResolver contentResolver = context.getContentResolver();


        ContentValues completeValues = new ContentValues();
        completeValues.put(Suppliers.COLUMN_NAME, InputTestUtils.Supplier_1.name);
        completeValues.put(Suppliers.COLUMN_SUPPLIER_EMAIL, InputTestUtils.Supplier_1.email);
        completeValues.put(Suppliers.COLUMN_SUPPLIER_PHONE, InputTestUtils.Supplier_1.phone);
        Uri insertedSupplier_Uri = contentResolver.insert(Suppliers.CONTENT_URI, completeValues);
        assert insertedSupplier_Uri != null;
        supplierId = (int) ContentUris.parseId(insertedSupplier_Uri);


        values_withoutPhone = new ContentValues(completeValues);
        values_withoutPhone.remove(Suppliers.COLUMN_SUPPLIER_PHONE);

        Uri insertedSupplier_withoutPhone_Uri = contentResolver.insert(Suppliers.CONTENT_URI, values_withoutPhone);
        assert insertedSupplier_withoutPhone_Uri != null;
        supplier_withoutPhone_id = (int) ContentUris.parseId(insertedSupplier_withoutPhone_Uri);


        values_withoutEmail = new ContentValues(completeValues);
        values_withoutEmail.remove(Suppliers.COLUMN_SUPPLIER_EMAIL);

        Uri insertedSupplier_WithoutEmail_Uri = contentResolver.insert(Suppliers.CONTENT_URI, values_withoutEmail);
        assert insertedSupplier_WithoutEmail_Uri != null;
        supplier_withoutEmail_id = (int) ContentUris.parseId(insertedSupplier_WithoutEmail_Uri);

    }

    @Before
    public void switchToSuppliersTab() {
        // Move to the suppliers tab
        onView(ViewMatchers.withId(R.id.navigation_suppliers)).perform(click());
    }

    @Test
    public void testCommunicationEmail() {
        // Move to the view holder with the supplier id without email
        onView(withId(R.id.suppliers_list))
                .perform(
                        RecyclerViewActions.actionOnHolderItem(
                                withSupplierId(supplierId),
                                clickChildViewWithId(R.id.orderButton)
                        )
                );

        onView(
                withText(
                        context.getString(R.string.choose_communication_question)
                )
        ).check(matches(isDisplayed()));

        // Click Email option
        onView(withId(android.R.id.button1)).perform(click());

        assertEmailIntent();
    }

    @Test
    public void testCommunicationSMS() {
        // Move to the view holder with the supplier id without email
        onView(withId(R.id.suppliers_list))
                .perform(
                        RecyclerViewActions.actionOnHolderItem(
                                withSupplierId(supplierId),
                                clickChildViewWithId(R.id.orderButton)
                        )
                );

        onView(
                withText(
                        context.getString(R.string.choose_communication_question)
                )
        ).check(matches(isDisplayed()));

        // Click SMS option
        onView(withId(android.R.id.button2)).perform(click());

        assertSMSIntent();
    }

    @Test
    public void testSupplierSMSIntent() {
        // Move to the view holder with the supplier id without email
        onView(withId(R.id.suppliers_list))
                .perform(
                        RecyclerViewActions.actionOnHolderItem(
                                withSupplierId(supplier_withoutEmail_id),
                                clickChildViewWithId(R.id.orderButton)
                        )
                );

        assertSMSIntent();
    }

    private void assertSMSIntent() {
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(
                        Uri.fromParts(
                                "sms",
                                values_withoutEmail.getAsString(Suppliers.COLUMN_SUPPLIER_PHONE),
                                null
                        )
                ),
                hasExtra("sms_body", context.getString(R.string.default_order_message)) // Todo move to settings
                )
        );
    }

    @Test
    public void testSupplierEmailIntent() {
        // Move to the view holder with the supplier id without email
        onView(withId(R.id.suppliers_list))
                .perform(
                        RecyclerViewActions.actionOnHolderItem(
                                withSupplierId(supplier_withoutPhone_id),
                                clickChildViewWithId(R.id.orderButton)
                        )
                );

        assertEmailIntent();
    }

    private void assertEmailIntent() {
        intended(allOf(
                hasAction(Intent.ACTION_SENDTO),
                hasData(Uri.parse("mailto:")),
                hasExtra(
                        Intent.EXTRA_EMAIL,
                        new String[]{values_withoutPhone.getAsString(Suppliers.COLUMN_SUPPLIER_EMAIL)}
                ),
                hasExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.default_email_subject)),  // TODO move to settings
                hasExtra(Intent.EXTRA_TEXT, context.getString(R.string.default_order_message)) // TODO move to settings
                )
        );
    }

}
