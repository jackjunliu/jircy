package example.jackliu.nonewfriends;

import android.support.test.espresso.ViewAssertion;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Rule;
import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.*;

/**
 * Created by irenepatt on 2/21/18.
 */


@RunWith(AndroidJUnit4.class)
public class SignupUnitTest extends ToastMatcher{
   @Rule
    public ActivityTestRule<SignupActivity> mActivityRule = new ActivityTestRule<>(SignupActivity.class);


   @Test
    public void test_no_password() {
       //enter unregistered email and leave out password field
       onView(withId(R.id.email_input)).perform(typeText("ireneeypatt@hotmail.com"), closeSoftKeyboard() );

       //check toast msg
       onView(withId(R.id.sign_up_button)).perform(click());
       onView(withText("Enter password!")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

   }

   @Test
    public void test_no_username() {
        //leave fields blank, and click to register
       onView(withId(R.id.sign_up_button)).perform(click());
       onView(withText("Please enter email address")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

   }

   @Test
    public void test_account_already_exists() {
        //use jack's account
       onView(withId(R.id.email_input)).perform(typeText("jiraiyaliu@gmail.com"), closeSoftKeyboard());
       onView(withId(R.id.password_input)).perform(typeText("jackjunliu"), closeSoftKeyboard());

       onView(withId(R.id.sign_up_button)).perform(click());
       onView(withText("Account already exists")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

   }

   @Test
    public void test_success() {
        onView(withId(R.id.email_input)).perform(typeText("ireenospatt@gmail.com"),closeSoftKeyboard());
       onView(withId(R.id.password_input)).perform(typeText("irenepawenapatt"), closeSoftKeyboard());

       onView(withId(R.id.sign_up_button)).perform(click());
       //should take to login activity
   }


}
