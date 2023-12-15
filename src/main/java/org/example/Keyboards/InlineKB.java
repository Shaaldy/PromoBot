package org.example.Keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKB {
    public static InlineKeyboardMarkup getInlineKeyBoard(long chatId, int currentPageNumber, int currentStore) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button0 = new InlineKeyboardButton();
        button0.setText("<<");
        button0.setCallbackData("prevStore_" + currentStore);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("<");
        button1.setCallbackData("prev_" + currentPageNumber);

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(">");
        button3.setCallbackData("next_" + currentPageNumber);

        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText(">>");
        button4.setCallbackData("nextStore_" + currentStore);


        row.add(button0);
        row.add(button1);
        row.add(button3);
        row.add(button4);
        keyboard.add(row);

        markup.setKeyboard(keyboard);

        return markup;
    }

}
