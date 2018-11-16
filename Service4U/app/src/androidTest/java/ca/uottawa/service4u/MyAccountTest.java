package ca.uottawa.service4u;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

public class MyAccountTest {
    @Rule
    public ActivityTestRule<MyServices> accountActivityTestI = new ActivityTestRule<>(MyServices.class);

    @Test
    public void AccountTest() {
        onData(anything()).inAdapterView(withId(R.id.listAllServices)) // Specify the explicit id of the ListView
                .atPosition(2) // Explicitly specify the adapter item to use
                .perform(click());
       // onView(R.id.button).perform(click());
    }
}
