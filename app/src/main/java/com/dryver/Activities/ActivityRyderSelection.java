/*
 * Copyright (C) 2016
 * Created by: usenka, jwu5, cdmacken, jvogel, asanche
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.dryver.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dryver.Controllers.RequestSingleton;
import com.dryver.Models.RequestStatus;
import com.dryver.R;
import com.dryver.Utility.HelpMe;
import com.dryver.Utility.ICallBack;

/**
 * The activity responsible for viewing a requests details more closely / inspecting a request.
 * Allows you to cancel requests, as well as view the drivers associated with a request.
 */

public class ActivityRyderSelection extends Activity {

    private TextView locationTextView;
    private TextView requestSelectionDate;
    private TextView statusTextView;

    private Button cancelButton;
    private Button viewDriversButton;
    private Button deleteButton;

    private RequestSingleton requestSingleton = RequestSingleton.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ryder_selection);

        locationTextView = (TextView) findViewById(R.id.requestSelectionLocation);
        requestSelectionDate = (TextView) findViewById(R.id.requestSelectionDate);
        statusTextView = (TextView) findViewById(R.id.requestSelectionToStatus);
        viewDriversButton = (Button) findViewById(R.id.requestSelectionButtonViewList);
        cancelButton = (Button) findViewById(R.id.requestSelectionButtonCancel);
        deleteButton = (Button) findViewById(R.id.requestSelectionButtonDelete);

        isCancelled();
        
        HelpMe.formatLocationTextView(requestSingleton.getTempRequest(), locationTextView);
        requestSelectionDate.setText("Request Date: " + HelpMe.getDateString(requestSingleton.getTempRequest().getDate()));
        statusTextView.setText("Status: " + requestSingleton.getTempRequest().statusCodeToString());

        setListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestSingleton.clearTempRequest();
    }

    private void setListeners(){

        //Clicking this opens the driver list through the controller
        viewDriversButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSingleton.viewRequestDrivers(ActivityRyderSelection.this, requestSingleton.getTempRequest());
            }
        });

        //Cancels the request
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSingleton.getTempRequest().setStatus(RequestStatus.CANCELLED);
                requestSingleton.pushTempRequest(new ICallBack() {
                    @Override
                    public void execute() {
                        finish();
                    }
                });
                isCancelled();
                statusTextView.setText("Status: " + requestSingleton.getTempRequest().statusCodeToString());
            }
        });

        //Deletes the request
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSingleton.removeRequest(requestSingleton.getTempRequest());
                statusTextView.setText("Status: " + requestSingleton.getTempRequest().statusCodeToString());
                finish();
            }
        });
    }

    /**
     * Checks if the request is already cancelled.
     */
    private void isCancelled() {
        if (requestSingleton.getTempRequest().getStatus().equals(RequestStatus.CANCELLED)) {
            cancelButton.setEnabled(false);
        }
    }
}
