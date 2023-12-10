package org.blitzcode.editor.helper;

import org.jline.reader.LineReader;

public class InputReader {

    private static final Character DEFAULT_MASK = '*';

    private Character mask;
    private LineReader lineReader;

    public InputReader(LineReader lineReader) {
        this(lineReader, null);
    }

    public InputReader(LineReader lineReader, Character mask) {
        this.lineReader = lineReader;
        this.mask = mask != null ? mask : DEFAULT_MASK;
    }

    public String prompt(String prompt) {
        return prompt(prompt, true);
    }

    public String prompt(String prompt, boolean echo) {
        String answer;
        if (echo) {
            answer = lineReader.readLine(prompt + ": ");
        } else {
            answer = lineReader.readLine(prompt + ": ", mask);
        }
        return answer;
    }
}