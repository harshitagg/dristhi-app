package org.ei.drishti.view.activity;

import android.test.ActivityInstrumentationTestCase2;
import org.ei.drishti.service.DrishtiService;
import org.ei.drishti.util.DrishtiSolo;
import org.ei.drishti.util.FakeDrishtiService;

import java.util.Date;

import static org.ei.drishti.util.FakeContext.setupService;
import static org.ei.drishti.util.Wait.waitForFilteringToFinish;
import static org.ei.drishti.util.Wait.waitForProgressBarToGoAway;

public class AlertsActivityTest extends ActivityInstrumentationTestCase2<AlertsActivity> {
    private FakeDrishtiService drishtiService;
    private DrishtiSolo solo;
    private String defaultSuffix;

    public AlertsActivityTest() {
        super(AlertsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        defaultSuffix = String.valueOf(new Date().getTime() - 1);
        drishtiService = new FakeDrishtiService(defaultSuffix);
        setupService(drishtiService, 1000000).updateApplicationContext(getActivity().getApplicationContext());

        solo = new DrishtiSolo(getInstrumentation(), getActivity());
    }

    public void testShouldLoadOnStartup() throws Throwable {
        solo.assertNamesInAlerts("Theresa 1 " + defaultSuffix, "Theresa 2 " + defaultSuffix);
    }

    public void testShouldUpdateWhenUpdateButtonInMenuIsPressed() throws Throwable {
        final String suffixForLoadingThroughMenuButton = String.valueOf(new Date().getTime());
        setupSuffix(suffixForLoadingThroughMenuButton);

        solo.updateUsingMenuButton();

        solo.assertNamesInAlerts("Theresa 1 " + suffixForLoadingThroughMenuButton, "Theresa 2 " + suffixForLoadingThroughMenuButton);
    }

    public void testShouldUpdateWhenPullToRefreshIsPerformed() throws Throwable {
        final String suffixForLoadingThroughMenuButton = String.valueOf(new Date().getTime());
        setupSuffix(suffixForLoadingThroughMenuButton);

        solo.updateUsingPullToRefresh();

        solo.assertNamesInAlerts("Theresa 1 " + suffixForLoadingThroughMenuButton, "Theresa 2 " + suffixForLoadingThroughMenuButton);
    }

    public void testShouldBeAbleToSwitchActivity() throws Exception {
        solo.assertCurrentActivity("Wrong activity", AlertsActivity.class);
        solo.assertNamesInAlerts("Theresa 1 " + defaultSuffix, "Theresa 2 " + defaultSuffix);

        solo.switchActivity();

        solo.assertCurrentActivity("Wrong activity", EligibleCoupleActivity.class);
        solo.assertNamesInECs("Wife 1 " + defaultSuffix, "Wife 2 " + defaultSuffix);
    }

    private DrishtiService setupSuffix(String suffix) {
        drishtiService.setSuffix(suffix);
        return drishtiService;
    }

    @Override
    public void tearDown() throws Exception {
        waitForFilteringToFinish();
        waitForProgressBarToGoAway(getActivity());
        solo.finishOpenedActivities();
    }
}