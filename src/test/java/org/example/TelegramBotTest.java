package org.example;

import org.example.TGBot.Command;
import org.junit.Assert;
import org.junit.Test;

public class TelegramBotTest{


    @Test
    public void testStart() {
        String ans = "Привет! Я Акция и готова помочь тебе найти товары по акции в нашем магазине. Просто напиши мне, что тебе нужно, и я с радостью помогу тебе найти лучшие предложения. А еще у меня есть много полезных советов и рекомендаций по выбору товаров. Не стесняйся, пиши мне /keyboard! \uD83D\uDE0A\uD83D\uDECD️";
        Command command = new Command();
        Assert.assertEquals(ans, command.getResponse("/start"));
    }

    @Test
    public void testHelp() {
        String ans = "Команды: /start - начать диалог с ботом, \n /store - ассортимент магазинов, \n /all - выводит весь список товаров по акции (доступны флаги -5, -p, -v), \n Название товара - выводит конкретный товар по акции, если он в наличии, \n Название магазина - выводит все товары по акции в этом магазине";
        Command command = new Command();
        Assert.assertEquals(ans, command.getResponse("/help"));
    }

    @Test
    public void testStore() {
        String ans = "Пятерочка, верный, перекресток. Какой магазин вас интересует?";
        Command command = new Command();
        Assert.assertEquals(ans, command.getResponse("/store"));
    }



    @Test
    public void testDefault() {
        String ans = "Извини, пока не можем предложить вам опцию, о которой вы спрашивали. 😔 Но не расстраивайтесь! Мы работаем над улучшением сервиса и добавлением новых возможностей. 🚀 Будем рады, если вы останетесь с нами и поддержите нас в этом. ❤️ Спасибо за ваше понимание!";
        Command command = new Command();
        Assert.assertEquals(ans, command.getResponse("default"));
    }
}