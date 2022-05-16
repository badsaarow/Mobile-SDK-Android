package com.dji.sdk.sample.internal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.demo.appactivation.AppActivationView;
import com.dji.sdk.sample.demo.battery.PushBatteryDataView;
import com.dji.sdk.sample.demo.battery.SetGetDischargeDayView;
import com.dji.sdk.sample.demo.datalocker.AccessLockerView;
import com.dji.sdk.sample.demo.flightcontroller.CompassCalibrationView;
import com.dji.sdk.sample.demo.flightcontroller.FlightAssistantPushDataView;
import com.dji.sdk.sample.demo.flightcontroller.FlightLimitationView;
import com.dji.sdk.sample.demo.flightcontroller.NetworkRTKView;
import com.dji.sdk.sample.demo.flightcontroller.OrientationModeView;
import com.dji.sdk.sample.demo.flightcontroller.VirtualStickView;
import com.dji.sdk.sample.demo.mobileremotecontroller.MobileRemoteControllerView;
import com.dji.sdk.sample.demo.remotecontroller.PushRemoteControllerDataView;
import com.dji.sdk.sample.internal.controller.DJISampleApplication;
import com.dji.sdk.sample.internal.controller.ExpandableListAdapter;
import com.dji.sdk.sample.internal.controller.MainActivity;
import com.dji.sdk.sample.internal.model.GroupHeader;
import com.dji.sdk.sample.internal.model.GroupItem;
import com.dji.sdk.sample.internal.model.ListItem;
import com.squareup.otto.Subscribe;

/**
 * This view is in charge of showing all the demos in a list.
 */

public class DemoListView extends FrameLayout {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expandableListView;

    public DemoListView(Context context) {
        this(context, null, 0);
    }

    public DemoListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DemoListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.demo_list_view, this);

        // Build model for ListView
        ListItem.ListBuilder builder = new ListItem.ListBuilder();

        builder.addGroup(R.string.component_listview_flight_controller,
                false,
                new GroupItem(R.string.flight_controller_listview_virtual_stick, VirtualStickView.class),
                new GroupItem(R.string.component_listview_mobile_remote_controller,
                        MobileRemoteControllerView.class),
                new GroupItem(R.string.flight_controller_listview_compass_calibration,
                        CompassCalibrationView.class),
                new GroupItem(R.string.flight_controller_listview_flight_limitation,
                        FlightLimitationView.class),
                new GroupItem(R.string.flight_controller_listview_orientation_mode, OrientationModeView.class),
                new GroupItem(R.string.flight_controller_listview_intelligent_flight_assistant,
                        FlightAssistantPushDataView.class),
                new GroupItem(R.string.flight_controller_listview_networkRTK, NetworkRTKView.class),
                new GroupItem(R.string.remote_controller_listview_push_info,
                        PushRemoteControllerDataView.class),
                new GroupItem(R.string.component_listview_app_activation,
                        AppActivationView.class));

        builder.addGroup(R.string.component_listview_battery,
                false,
                new GroupItem(R.string.battery_listview_push_info, PushBatteryDataView.class),
                new GroupItem(R.string.battery_listview_set_get_discharge_day, SetGetDischargeDayView.class));


        // Set-up ExpandableListView
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_list);
        listAdapter = new ExpandableListAdapter(context, builder.build());
        expandableListView.setAdapter(listAdapter);
        DJISampleApplication.getEventBus().register(this);
        expandAllGroupIfNeeded();
    }

    @Subscribe
    public void onSearchQueryEvent(MainActivity.SearchQueryEvent event) {
        listAdapter.filterData(event.getQuery());
        expandAllGroup();
    }

    /**
     * Expands all the group that has children
     */
    private void expandAllGroup() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.expandGroup(i);
        }
    }

    /**
     * Expands all the group that has children
     */
    private void expandAllGroupIfNeeded() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            if (listAdapter.getGroup(i) instanceof GroupHeader
                    && ((GroupHeader) listAdapter.getGroup(i)).shouldCollapseByDefault()) {
                expandableListView.collapseGroup(i);
            } else {
                expandableListView.expandGroup(i);
            }
        }
    }
}
