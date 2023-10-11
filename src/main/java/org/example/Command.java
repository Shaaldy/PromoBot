package org.example;


import java.util.HashMap;
import java.util.Map;

public class Command {
    private Map<String, String> commands;
    public Command(){
        commands = new HashMap<>();
        //добавляем комманды
        commands.put("/start", "Привет! Я Акция и готова помочь тебе найти товары по акции в нашем магазине. Просто напиши мне, что тебе нужно, и я с радостью помогу тебе найти лучшие предложения. А еще у меня есть много полезных советов и рекомендаций по выбору товаров. Не стесняйся, пиши мне! \uD83D\uDE0A\uD83D\uDECD\uFE0F");
        commands.put("/help", "Команды: /start - начать диалог с ботом, /store - ассортимент магазинов");
        commands.put("/store", "Магнит (пока в разработке)");

        commands.put("default", "Извини, пока не можем предложить вам опцию, о которой вы спрашивали. 😔 Но не расстраивайтесь! Мы работаем над улучшением сервиса и добавлением новых возможностей. 🚀 Будем рады, если вы останетесь с нами и поддержите нас в этом. ❤️ Спасибо за ваше понимание!");

}
    public String getResponse(String command){
        return commands.get(command);
    }

    public boolean hasCommand(String command){
        return commands.containsKey(command);
    }
}