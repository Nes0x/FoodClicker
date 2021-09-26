package me.nes0x.utilities;

import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;
import com.besaba.revonline.pastebinapi.response.Response;

import javax.swing.*;


public class Changelog {


    public Changelog(JPanel panel) {
        final PastebinFactory factory = new PastebinFactory();
        final Pastebin pastebin = factory.createPastebin("");
        final String pasteKey = "";
        final Response<String> pasteResponse = pastebin.getRawPaste(pasteKey);

        if (pasteResponse.hasError()) {
            return;
        }

        JTextArea text = new JTextArea();
        text.setText(pasteResponse.get());
        text.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(text);
        panel.add(scrollPane);

    }
}
