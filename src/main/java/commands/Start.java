package commands;

import core.State;
import org.telegram.telegrambots.meta.api.objects.Update;

import static utils.ConstantStrings.*;

public class Start implements Command {

    @Override
    public String perform(String[] args, Update update, State state) {
        return GREETINGS_MESSAGE + "\n\n" +
                "Для регистрации нажми на кнопку " + SET_NAME_BUTTON;
    }
}
