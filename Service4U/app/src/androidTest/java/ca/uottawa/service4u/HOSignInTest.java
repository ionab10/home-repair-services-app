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
public class HOSignInTest {
    @Rule
    public ActivityTestRule<ca.uottawa.service4u.MainActivity> signInTest = new ActivityTestRule<>(MainActivity.class);
    @Test
    public void SignInAsHomeowner(){
        onView(withId(R.id.fieldEmail)).perform(typeText("home@owner.com"), closeSoftKeyboard());
        onView(withId(R.id.fieldPassword)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.emailSignInButton)).perform(click()); //should sign in as homeowner
    }
}
