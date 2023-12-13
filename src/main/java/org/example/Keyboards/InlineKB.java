package org.example.Keyboards;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKB {
    public static InlineKeyboardMarkup getInlineKeyBoard(int pageNumber){

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("<");
        button1.setCallbackData("prev_" + pageNumber);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(">");
        button2.setCallbackData("next_" + pageNumber);


        row.add(button1);
        row.add(button2);
        keyboard.add(row);

        markup.setKeyboard(keyboard);

        return markup;

    }

    public void setInlineKB(SendMessage sendMessage){
        InlineKeyboardMarkup markup = getInlineKeyBoard(1);
        sendMessage.setReplyMarkup(markup);
    }

}
