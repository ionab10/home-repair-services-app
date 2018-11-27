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
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;

public class HOSearchTest {
    @Rule
    public ActivityTestRule<BookJobActivity> search = new ActivityTestRule<>(BookJobActivity.class);

    @Test
    public void SearchTest(){ //checks spinner
        onView(withId(R.id.serviceSpinner)).perform(click());
        onData(anything()).atPosition(7).perform(click()); // Explicitly specify the adapter item to use
        onView(withId(R.id.serviceSpinner)).check(matches(withSpinnerText(containsString("IT - Rogers"))));
    }
}
