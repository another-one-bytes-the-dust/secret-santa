package core;

import commands.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import utils.DateTimeUtils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static utils.ConstantStrings.*;
import static utils.MessageUtils.buildContextKeyboard;

public class Bot extends TelegramLongPollingBot {

    private static State state;

    public static void main(String[] args) {
        try {
            state = State.REGISTRATION;
            TelegramBotsApi botApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot = new Bot();
            botApi.registerBot(bot);

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

            executor.scheduleAtFixedRate(() -> {
                if (state == State.REGISTRATION
                        && DateTimeUtils.isRevealDate()) {
                    state = State.REVEAL;
                    bot.revealTime();
                }}, 0, 1, TimeUnit.MINUTES);

            executor.scheduleAtFixedRate(() -> {
                if (state == State.REVEAL
                        && DateTimeUtils.isGiftGivingDate()) {
                    state = State.GIFT_TIME;
                    bot.giftTime();
                }}, 0, 1, TimeUnit.MINUTES);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return System.getenv("BOT_USERNAME");
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) return;

        Message message = update.getMessage();
        String chatId = message.getChat().getId() + "";
        String rawMessage = message.getText();

        String[] args = rawMessage.split(" ");

        // postponed commands
        if (Commands.postponedMap.containsKey(chatId)) {
            String response = Commands.postponedMap.get(chatId).perform(args, update, state);

            if (response == null) return;
            executeTraced(messageWithKeyboard(chatId, response));
            Commands.postponedMap.remove(chatId);
            return;
        }

        // other types of commands
        Command command = Commands.commandMap.getOrDefault(rawMessage, null);
        if (command != null) {
            String response = command.perform(args, update, state);

            if (response == null) return;
            executeTraced(defaultMessage(chatId, response));
        }
    }

    public void revealTime() {
        Database database = new Database();

        new AssignSantas().perform(null, null, null);
        List<Participant> participants = database.getAllParticipants();

        for (Participant p : participants)
            executeTraced(messageWithKeyboard(p.getChatId(), REVEAL_NOTIFY));
    }

    public void giftTime() {
        Database database = new Database();

        List<Participant> participants = database.getAllParticipants();

        for (Participant p : participants)
            executeTraced(messageWithKeyboard(p.getChatId(), GIFT_NOTIFY));
    }

    public SendMessage defaultMessage(String chatId, String text) {
        return SendMessage.builder().text(text).chatId(chatId).build();
    }

    public static SendMessage messageWithKeyboard(String chatId, String text) {
        SendMessage message =
                SendMessage.builder()
                        .chatId(chatId)
                        .text(text)
                        .build();
        message.setReplyMarkup(buildContextKeyboard(state));
        return message;
    }

    public void executeTraced(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
