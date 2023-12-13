package org.example.Keyboards;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboard {

    public static ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton();
        keyboardButton1.setText("Пятерочка");

        KeyboardButton keyboardButton2 = new KeyboardButton();
        keyboardButton2.setText("Верный");

        KeyboardButton keyboardButton3 = new KeyboardButton();
        keyboardButton3.setText("Перекресток");

        keyboardRow1.add(keyboardButton1);
        keyboardRow1.add(keyboardButton2);
        keyboardRow1.add(keyboardButton3);
        keyboardRowList.add(keyboardRow1);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;
    }

    public void setReplyKeyboard(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
}
