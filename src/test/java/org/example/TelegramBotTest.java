package org.example;

import org.example.TGBot.Command;
import org.junit.Assert;
import org.junit.Test;

public class TelegramBotTest{


    @Test
    public void testStart() {
        String ans = "Привет! Я Акция и готова помочь тебе найти товары по акции в нашем магазине. Просто напиши мне, что тебе нужно, и я с радостью помогу тебе найти лучшие предложения. А еще у меня есть много полезных советов и рекомендаций по выбору товаров. Не стесняйся, пиши мне /help! \uD83D\uDE0A\uD83D\uDECD️";
        Command command = new Command();
        Assert.assertEquals(ans, command.getResponse("/start"));
    }

    @Test
    public void testHelp() {
        String ans = "Команды: /start - начать диалог с ботом, \n /store - ассортимент магазинов, \n для того чтобы выбрать магазин и товар мы можете в виде списка написать магазины и через @ написать товар, который вам необходим. \nПример: Пятерочка Магнит @ чай.\n поддерживается запрос и без @, тогда указывайте только магазины или только товар";
        Command command = new Command();
        Assert.assertEquals(ans, command.getResponse("/help"));
    }

    @Test
    public void testStore() {
        String ans = "Пятерочка 🛒\nМагнит 🏪\nВерный 🛒\nКБ 🏪\nАленка 🛒\nАриант 🏪\nБристоль 🥃\nПивко 🍺\nКировский 🛒\nМонетка 🛒\n. Какой магазин вас интересует?";
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