package commands;

import core.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Help implements Command {

    @Override
    public String perform(String[] args, Update update, State state) {
        return "Поменять имя - /name [твоё новое ФИО]\n" +
                "Сменить отдел - /department [отдел мечты]\n";
    }
}
