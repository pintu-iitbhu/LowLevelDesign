package com.rcpky.button;

import com.rcpky.enums.ButtonType;

public class ButtonFactory {


    Button getButton(ButtonType buttonType) {
        return new FanButton();
    }
}
