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

public class HOSearchCheckBTest {
    @Rule
    public ActivityTestRule<BookJobActivity> checkButton= new ActivityTestRule<>(BookJobActivity.class);

    @Test
    public void CheckButtonTest(){
        onView(withId(R.id.sundayMorningCB)).perform(click()); //checks if there is a service provider available sunday or monday morning
        onView(withId(R.id.mondayMorningCB)).perform(click());
        onView(withId(R.id.findAProviderBtn)).perform(click());
    }
}
