package ca.uottawa.service4u;

import org.junit.Rule;
import org.junit.Test;

import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
public class MainTest {
    @Rule
    public ActivityTestRule<ca.uottawa.service4u.MainActivity> signInTestI = new ActivityTestRule<>(MainActivity.class);
    @Test
    public void invalidSignIn(){ //should fail
        onView(withId(R.id.fieldEmail)).perform(typeText("fakeemail@hotmail.com"), closeSoftKeyboard());
        onView(withId(R.id.fieldPassword)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.emailSignInButton)).perform(click()); //should fail
        //onView(withText("john smith")).check(matches(isDisplayed()));
    }/*
    // @Rule
    // public ActivityTestRule<ca.uottawa.service4u.MainActivity> signInTestV= new ActivityTestRule<>(MainActivity.class);
    @Test
    public void validSignInTest() { //example of valid sign in not necessary so commented out
        onView(withId(R.id.fieldEmail)).perform(typeText("johnsmith@hotmail.com"), closeSoftKeyboard());
        onView(withId(R.id.fieldPassword)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.emailSignInButton)).perform(click()); //should pass
        // onView(withText("john smith")).check(matches(isDisplayed()));
    }*/

}

