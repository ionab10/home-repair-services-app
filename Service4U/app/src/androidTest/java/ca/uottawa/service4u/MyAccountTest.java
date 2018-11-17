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
    public ActivityTestRule<MyAccountActivity> accountActivityTestI = new ActivityTestRule<>(MyAccountActivity.class);

    @Test
    public void AccountTest() { //signed in as service provider
        onView(withId(R.id.editAccountBtn)).perform(click());
        onView(withId(R.id.editPhoneNumber)).perform(typeText("1"), closeSoftKeyboard()); //adds too many numbers to phone number
        onView(withId(R.id.editAccountBtn)).perform(click());
      /*
        onData(anything()).inAdapterView(withId(R.id.listAllServices)) // Specify the explicit id of the ListView
                .atPosition(2) // Explicitly specify the adapter item to use
                .perform(click());
       // onView(R.id.button).perform(click());*/
    }


}
