package ca.uottawa.service4u;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import android.support.test.rule.ActivityTestRule;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class CreateActivityTest {
    @Rule
    public ActivityTestRule<CreateAccountActivity> createAccountTestI = new ActivityTestRule<>(CreateAccountActivity.class);

    @Test
    public void createAccountInValid() { //wont be able to create account
        onView(withId(R.id.fieldFirstName)).perform(typeText("John"), closeSoftKeyboard());
        onView(withId(R.id.fieldLastName)).perform(typeText("Snow"), closeSoftKeyboard());
        onView(withId(R.id.fieldEmail2)).perform(typeText("winterisc@ming.ca"), closeSoftKeyboard());
        onView(withId(R.id.fieldPassword2)).perform(typeText("Stark4Eva"), closeSoftKeyboard());
        // onView(withId(R.id.fieldPassword3)).perform(typeText("Stark4Eva"), closeSoftKeyboard());
        onView(withId(R.id.serviceProvRadioBtn)).perform(click());
        onView(withId(R.id.fieldPhone)).perform(typeText("5193769292"));

        onView(withId(R.id.emailCreateAccountButton2)).perform(click());
        // onView(withText("admin already created")).check(matches(isDisplayed()));
    }/*
    //  @Rule
    //  public ActivityTestRule<ca.uottawa.service4u.CreateAccountActivity> createAccountTestV = new ActivityTestRule<>(CreateAccountActivity.class);
    @Test
    public void createAccountValidTest(){ //example of working case not necessary so commented out
        onView(withId(R.id.fieldFirstName)).perform(typeText("John"), closeSoftKeyboard());
        onView(withId(R.id.fieldLastName)).perform(typeText("Snow"), closeSoftKeyboard());
        onView(withId(R.id.fieldEmail2)).perform(typeText("winteris@coming.ca"), closeSoftKeyboard());
        onView(withId(R.id.fieldPassword2)).perform(typeText("Stark4Eva"), closeSoftKeyboard());
        onView(withId(R.id.fieldPassword3)).perform(typeText("Stark4Eva"), closeSoftKeyboard());
        }
        */
}