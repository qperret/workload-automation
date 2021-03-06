/*    Copyright 2013-2015 ARM Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/


package com.arm.wlauto.uiauto.caffeinemark;

import android.app.Activity;
import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import com.arm.wlauto.uiauto.BaseUiAutomation;

import org.junit.Test;
import org.junit.runner.RunWith;

// Import the uiautomator libraries

@RunWith(AndroidJUnit4.class)
public class UiAutomation extends BaseUiAutomation {

    public static String TAG = "caffeinemark";
    public String[] categories = {"Sieve", "Loop", "Logic", "String", "Float", "Method"};

@Test
public void runUiAutomation() throws Exception {
        initialize_instrumentation();
        Bundle status = new Bundle();
        status.putString("product", mDevice.getProductName());

        UiSelector selector = new UiSelector();
        UiObject runButton = mDevice.findObject(selector.text("Run benchmark")
                                                  .className("android.widget.Button"));
        runButton.click();

        try {
            waitText("CaffeineMark results");
            extractOverallScore();
            extractDetailedScores();


        } catch(UiObjectNotFoundException e) {
            takeScreenshot("caffeine-error");
        }

        mInstrumentation.sendStatus(Activity.RESULT_OK, status);
    }

    public void extractOverallScore() throws Exception {
        UiSelector selector = new UiSelector();
        UiObject linearLayoutOverallScore = mDevice.findObject(selector.className("android.widget.LinearLayout")
                                                                        .instance(1));
        UiObject overallScore = linearLayoutOverallScore.getChild(selector.className("android.widget.TextView")
                                                                          .instance(2));
        Log.v(TAG, "CAFFEINEMARK RESULT: OverallScore " + overallScore.getText());
    }

    public void extractDetailedScores() throws Exception {
        UiSelector selector = new UiSelector();
        UiObject detailsButton = mDevice.findObject(selector.text("Details")
                                                      .className("android.widget.Button"));
        detailsButton.click();
        sleep(2);

        UiObject linearObject;
        UiObject detailedScore;
        for (int i = 1; i <= 6; i++) {
          linearObject = mDevice.findObject(selector.className("android.widget.LinearLayout")
                                              .instance(i));
          detailedScore = linearObject.getChild(selector.className("android.widget.TextView")
                                                        .instance(1));
          Log.v(TAG,"CAFFEINEMARK RESULT: " + categories[i-1] + " " + detailedScore.getText());
        }
    }
}
