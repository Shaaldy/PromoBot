package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class TelegramBot extends TelegramLongPollingBot {

    private String botToken;
    private String botName;

    public TelegramBot()
    {
        Properties props = new Properties();
        try(InputStream input = TelegramBot.class.getClassLoader().getResourceAsStream("config.properties"))
        {
            props.load(input);
        }catch (IOException e){
            e.printStackTrace();
        }
        String token = props.getProperty("bot.token");
        this.botToken = token;
        String name = props.getProperty("bot.name");
        this.botName = name;
    }
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText())
        {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText)
            {
                case ("/start"):
                    try {
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case ("/help"):
                    String menu = "Команды: /start - начать диалог с ботом, /store - ассортимент магазинов";
                    sendMessage(chatId, menu);
                    break;

                case ("/store"):
                    String stores = "Магнит (пока в разработке)";
                    sendMessage(chatId, stores);
                    break;
                default:
                    String text = "Извини, пока не можем предложить вам опцию, о которой вы спрашивали. \uD83D\uDE14 Но не расстраивайтесь! Мы работаем над улучшением сервиса и добавлением новых возможностей. \uD83D\uDE80 Будем рады, если вы останетесь с нами и поддержите нас в этом. ❤\uFE0F Спасибо за ваше понимание!";
                    sendMessage(chatId, text);
                    break;
            }
        }
    }

    private void startCommandReceived(long chatId, String name) throws TelegramApiException
    {
        String answer = "Привет! " + name + ", Я Акция и готова помочь тебе найти товары по акции в нашем магазине. Просто напиши мне, что тебе нужно, и я с радостью помогу тебе найти лучшие предложения. А еще у меня есть много полезных советов и рекомендаций по выбору товаров. Не стесняйся, пиши мне! \uD83D\uDE0A\uD83D\uDECD\uFE0F";
        sendMessage(chatId, answer);
    }
    private void sendMessage(long chatId, String textToSend)
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);

        try
        {
            execute(sendMessage);
        }
        catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getBotUsername() {
        return botName;
    }
    @Override
    public String getBotToken()
    {

        return botToken;

    }
}
