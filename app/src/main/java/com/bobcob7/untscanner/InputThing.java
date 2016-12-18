package com.bobcob7.untscanner;

import android.content.res.Configuration;
import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by macbook on 12/18/16.
 */

public class InputThing extends InputMethodService {
    @Override
    public boolean onEvaluateInputViewShown() {
        Configuration config = getResources().getConfiguration();
        return config.keyboard == Configuration.KEYBOARD_NOKEYS
                || config.hardKeyboardHidden == Configuration.KEYBOARDHIDDEN_YES;
    }
}
