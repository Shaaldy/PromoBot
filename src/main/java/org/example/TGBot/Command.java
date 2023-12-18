package org.example.TGBot;


import java.util.HashMap;
import java.util.Map;

public class Command {
    private final Map<String, String> commands;

    public Command() {
        commands = new HashMap<>();
        commands.put("/start", "Привет! Я Акция и готова помочь тебе найти товары по акции в нашем магазине. Просто напиши мне, что тебе нужно, и я с радостью помогу тебе найти лучшие предложения. А еще у меня есть много полезных советов и рекомендаций по выбору товаров. Не стесняйся, пиши мне /help! \uD83D\uDE0A\uD83D\uDECD️");
        commands.put("/help", "Команды: /start - начать диалог с ботом, \n /store - ассортимент магазинов, \n для того чтобы выбрать магазин и товар мы можете в виде списка написать магазины и через @ написать товар, который вам необходим. \nПример: Пятерочка Магнит @ чай.\n поддерживается запрос и без @, тогда указывайте только магазины или только товар");
        commands.put("/store", "Пятерочка 🛒\nМагнит 🏪\nВерный 🛒\nКБ 🏪\nАленка 🛒\nАриант 🏪\nБристоль 🥃\nПивко 🍺\nКировский 🛒\nМонетка 🛒\n. Какой магазин вас интересует?");
        commands.put("default", "Извини, пока не можем предложить вам опцию, о которой вы спрашивали. 😔 Но не расстраивайтесь! Мы работаем над улучшением сервиса и добавлением новых возможностей. 🚀 Будем рады, если вы останетесь с нами и поддержите нас в этом. ❤️ Спасибо за ваше понимание!");

    }

    public String getResponse(String command) {
        return commands.get(command);
    }

    public boolean hasCommand(String command) {
        return commands.containsKey(command);
    }

}
