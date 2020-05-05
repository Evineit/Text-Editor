package editor;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchRegex extends JTextField {

    public SearchRegex(String text, int columns) {
        super(text, columns);
    }
    public Matcher getMatches(String pattern, String text){
        Pattern pattern1 = Pattern.compile(pattern);
        return pattern1.matcher(text);

    }
}
