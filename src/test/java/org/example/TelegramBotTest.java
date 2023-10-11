package org.example;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class TelegramBotTest{


    @Test
    public void testStart() {
        TelegramBot bot = new TelegramBot();
        String ans = "Привет! Я Акция и готова помочь тебе найти товары по акции в нашем магазине. Просто напиши мне, что тебе нужно, и я с радостью помогу тебе найти лучшие предложения. А еще у меня есть много полезных советов и рекомендаций по выбору товаров. Не стесняйся, пиши мне! \uD83D\uDE0A\uD83D\uDECD\uFE0F";
        Command command = new Command();
        Assert.assertEquals(ans, command.getResponse("/start"));
    }

    @Test
    public void testHelp() {
        TelegramBot bot = new TelegramBot();
        String ans = "Команды: /start - начать диалог с ботом, /store - ассортимент магазинов";
        Command command = new Command();
        Assert.assertEquals(ans, command.getResponse("/help"));
    }

    @Test
    public void testStore() {
        TelegramBot bot = new TelegramBot();
        String ans = "Магнит (пока в разработке)";
        Command command = new Command();
        Assert.assertEquals(ans, command.getResponse("/store"));
    }

    @Test
    public void testDefault() {
        TelegramBot bot = new TelegramBot();
        String ans = "Извини, пока не можем предложить вам опцию, о которой вы спрашивали. 😔 Но не расстраивайтесь! Мы работаем над улучшением сервиса и добавлением новых возможностей. 🚀 Будем рады, если вы останетесь с нами и поддержите нас в этом. ❤️ Спасибо за ваше понимание!";
        Command command = new Command();
        Assert.assertEquals(ans, command.getResponse("default"));
    }
}