package editor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchRegex extends JTextField {

    public SearchRegex(String text, int columns) {
        super(text, columns);
    }
    public Matcher getMatches(String pattern, String text){
        Pattern javaPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        return javaPattern.matcher(text);

    }
}
