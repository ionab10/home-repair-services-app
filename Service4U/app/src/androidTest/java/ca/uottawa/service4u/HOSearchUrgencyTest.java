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

public class HOSearchUrgencyTest {
    @Rule
    public ActivityTestRule<BookJobActivity> checkUrgency = new ActivityTestRule<>(BookJobActivity.class);

    @Test
    public void CheckUrgencyTest(){
        onView(withId(R.id.sundayMorningCB)).perform(click()); //checks if there is a service provider available sunday or monday morning
        onView(withId(R.id.mondayMorningCB)).perform(click());
        onView(withId(R.id.urgencySpinner)).perform(click());
        onData(anything()).atPosition(2).perform(click()); //sets the urgency to 2 weeks notice
        onView(withId(R.id.timeSpinner)).perform(click());
        onData(anything()).atPosition(2).perform(click()); //sets time to 1.5 hours
        onView(withId(R.id.findAProviderBtn)).perform(click());
    }
}
