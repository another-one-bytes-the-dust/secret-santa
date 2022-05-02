package utils;

import core.State;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static utils.ConstantStrings.*;

public class MessageUtils {

    public static ReplyKeyboardMarkup buildContextKeyboard(State state) {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboard;

        if (state == State.REGISTRATION) {
            KeyboardRow firstRow = new KeyboardRow();
            KeyboardRow secondRow = new KeyboardRow();

            firstRow.add(SET_NAME_BUTTON);
            firstRow.add(SET_DEPARTMENT_BUTTON);
            secondRow.add(REGISTER_BUTTON);

            keyboard = ReplyKeyboardMarkup.builder()
                    .keyboardRow(firstRow).keyboardRow(secondRow);
        } else {
            KeyboardRow row = new KeyboardRow();
            row.add(GET_RECEIVER_BUTTON);
            keyboard = ReplyKeyboardMarkup.builder()
                    .keyboardRow(row);
        }

        return keyboard.resizeKeyboard(true).build();
    }
}
