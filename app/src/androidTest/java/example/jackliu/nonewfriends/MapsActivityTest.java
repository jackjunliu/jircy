package example.jackliu.nonewfriends;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by yoonlee on 2/17/18.
 */
@RunWith(AndroidJUnit4.class)
public class MapsActivityTest {
    private UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());

    @Rule
    public ActivityTestRule<MapsActivity> mActivityRule = new ActivityTestRule<>(
            MapsActivity.class);

    @Test
    public void getTestMarker() {
        UiObject mMarker1 = uiDevice.findObject(new UiSelector().descriptionContains("Test_Stub_1"));
        Assert.assertTrue(mMarker1.exists());
    }

    @Test
    public void fakeMarker() {
        UiObject mMarker1 = uiDevice.findObject(new UiSelector().descriptionContains("fakeName"));
        Assert.assertTrue(!mMarker1.exists());
    }
}