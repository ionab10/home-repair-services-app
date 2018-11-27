package ca.uottawa.service4u;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
public final class HOSearchRatingTest  {
    @Rule
    public ActivityTestRule<BookJobActivity> checkRating = new ActivityTestRule<>(BookJobActivity.class);

    @Test
    public void CheckRatingTest() {
        onView(withId(R.id.sundayMorningCB)).perform(click()); //checks if there is a service provider available sunday or monday morning
        onView(withId(R.id.mondayMorningCB)).perform(click());
        onView(withId(R.id.minRatingBar)).perform(click()); //checks for a minimum rating of just over half max rating
        onView(withId(R.id.findAProviderBtn)).perform(click());
    }
}
