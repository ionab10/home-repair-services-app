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
        onView(withId(R.id.nameField)).perform(typeText("New wifi router"), closeSoftKeyboard()); //adding service
        // onView(withId(R.id.typeField)).perform(withSpinnerText());
        // onView(withId(R.id.typeField)).perform(Spinner.drop)
        onView(withId(R.id.rateField)).perform(typeText("9.5")); //must be atleast 10
        onView(withId(R.id.addServiceBtn)).perform(click());
    } /*
    // @Rule
    //   public ActivityTestRule<ca.uottawa.service4u.ServicesActivity> adminAddServiceV = new ActivityTestRule<>(ServicesActivity.class);
    @Test
    public void serviceActivityValid(){ //example of it working commented out for now not necessary
        onView(withId(R.id.newServiceBtn)).perform(click());
        onView(withId(R.id.nameField)).perform(typeText("New wifi router"), closeSoftKeyboard());
        // onView(withId(R.id.typeField)).perform(withSpinnerText());
        // onView(withId(R.id.typeField)).perform(Spinner.drop)
        onView(withId(R.id.rateField)).perform(typeText("15"));
        onView(withId(R.id.addServiceBtn)).perform(click());
    }*/

}
