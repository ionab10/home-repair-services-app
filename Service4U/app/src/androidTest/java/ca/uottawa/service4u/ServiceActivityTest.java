package ca.uottawa.service4u;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class ServiceActivityTest {
    @Rule
    public ActivityTestRule<ServicesActivity> adminAddServiceI = new ActivityTestRule<>(ServicesActivity.class);
    @Test
    public void serviceActivityInvalid(){
        onView(withId(R.id.newServiceBtn)).perform(click());
        onView(withId(R.id.nameField)).perform(typeText("Google Home Installation"), closeSoftKeyboard()); //adding service
        // onView(withId(R.id.typeField)).perform(withSpinnerText());
        // onView(withId(R.id.typeField)).perform(Spinner.drop)
        onView(withId(R.id.rateField)).perform(typeText("9.5")); //must be atleast 10
        onView(withId(R.id.addServiceBtn)).perform(click());
    }

}
