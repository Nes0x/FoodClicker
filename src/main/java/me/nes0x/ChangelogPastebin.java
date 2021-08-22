package me.nes0x;

import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;
import com.besaba.revonline.pastebinapi.response.Response;

import javax.swing.*;


public class ChangelogPastebin {


    public ChangelogPastebin(JPanel panel) {
        final PastebinFactory factory = new PastebinFactory();
        final Pastebin pastebin = factory.createPastebin("");
        final String pasteKey = "";
        final Response<String> pasteResponse = pastebin.getRawPaste(pasteKey);

        if (pasteResponse.hasError()) {
            return;
        }

        JTextArea text = new JTextArea(10, 10);
        text.setText(pasteResponse.get());
        text.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(text);
        panel.add(scrollPane);

    }
}
