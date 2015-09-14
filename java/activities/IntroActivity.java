/**
 * Copyright (C) 2014 Twitter Inc and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package activities;


import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

import com.spleat.R;
import managers.ActivityManager;
import Utils.GetProductsTask;


/**
 * Created by guillaumeagis on 21/04/15.
 *
 * Intro page, display the logo and load the venues before going to the next view.
 *  The act Manager is used to centralize all the actions related to the creation and destruction of activities.
 */
public class IntroActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        setupCountDown();
        new GetProductsTask(null, true).execute();
    }


    /**
     * Display the intro page during 2sec and display the list of venues
     */
    public void setupCountDown()
    {
        final ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        // Move into main app after a delay
        final int pms = 2500;
        new CountDownTimer(pms, 100) {

            public void onTick(long millisUntilFinished) {

                long currentTime = pms - millisUntilFinished;
                int percentage = (int) ((currentTime * 100) / pms);
                progressBar.setProgress(percentage);
            }

            public void onFinish() {
                ActivityManager.push(IntroActivity.this, ActivityManager.eType.LIST);
                IntroActivity.this.finish();
            }
        }.start();
    }

}
