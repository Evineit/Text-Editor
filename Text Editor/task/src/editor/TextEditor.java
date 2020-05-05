package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.Matcher;

public class TextEditor extends JFrame {
    ArrayList<String> matches = new ArrayList<>();
    ArrayList<Integer> index = new ArrayList<>();
    File file;
    JPanel topPanel = new JPanel();
    JPanel mainPanel = new JPanel();
    JButton buttonLoad = new JButton("Open");
    JButton buttonSave = new JButton("Save");
    SearchRegex searchField = new SearchRegex("",5);
    JTextArea textArea = new JTextArea(5,4);
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem menuItemOpen = new JMenuItem("Open");
    JMenuItem menuItemSave = new JMenuItem("Save");
    JMenuItem menuItemExit = new JMenuItem("Exit");
    JButton startSearchButton = new JButton("Search");
    JButton prevSearchButton = new JButton("<");
    JButton nextSearchButton = new JButton(">");
    JCheckBox RegExCheckbox = new JCheckBox("Regex");
    JFileChooser fileChooser = new JFileChooser();
    JMenu searchMenu = new JMenu("Search");
    JMenuItem menuItemStartSearch = new JMenuItem("Start Search");
    JMenuItem menuItemPrevSearch = new JMenuItem("Previous Search");
    JMenuItem menuItemNextSearch = new JMenuItem("Next Search");
    JMenuItem menuItemUseRegex = new JMenuItem("Use Regex");

    static String TITLE = "Text editor :)";

    public TextEditor() {
        super(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        add(fileChooser);
        add(topPanel,BorderLayout.NORTH);
        add(mainPanel,BorderLayout.CENTER);
//        fileChooser.setVisible(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.X_AXIS));
        mainPanel.setLayout(new BorderLayout());

        textArea.setName("TextArea");
        searchField.setName("SearchField");

        buttonSave.setName("SaveButton");
        buttonLoad.setName("OpenButton");
        startSearchButton.setName("StartSearchButton");
        prevSearchButton.setName("PreviousMatchButton");
        nextSearchButton.setName("NextMatchButton");
        RegExCheckbox.setName("UseRegExCheckbox");
        fileChooser.setName("FileChooser");
        scrollPane.setName("ScrollPane");
        menu.setName("MenuFile");
        searchMenu.setName("MenuSearch");
        menuItemOpen.setName("MenuOpen");
        menuItemSave.setName("MenuSave");
        menuItemExit.setName("MenuExit");
        menuItemStartSearch.setName("MenuStartSearch");
        menuItemPrevSearch.setName("MenuPreviousMatch");
        menuItemNextSearch.setName("MenuNextMatch");
        menuItemUseRegex.setName("MenuUseRegExp");

        setJMenuBar(menuBar);
        menuBar.add(menu);
        menu.add(menuItemOpen);
        menu.add(menuItemSave);
        menu.addSeparator();
        menu.add(menuItemExit);
        menuBar.add(searchMenu);
        searchMenu.add(menuItemStartSearch);
        searchMenu.add(menuItemPrevSearch);
        searchMenu.add(menuItemNextSearch);
        searchMenu.add(menuItemUseRegex);

        topPanel.add(buttonLoad);
        topPanel.add(buttonSave);
        topPanel.add(searchField);
        topPanel.add(startSearchButton);
        topPanel.add(prevSearchButton);
        topPanel.add(nextSearchButton);
        topPanel.add(RegExCheckbox);
        mainPanel.add(scrollPane);


        scrollPane.setMinimumSize(new Dimension(200,100));
        scrollPane.setPreferredSize(new Dimension(200,100));
        buttonLoad.addActionListener(actionEvent -> {
            openFile();
        });
        buttonSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveFile();
            }
        });
        menuItemSave.addActionListener(actionEvent -> {
            saveFile();
        });
        menuItemOpen.addActionListener(actionEvent -> {
            openFile();
        });
        menuItemExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                System.exit(0);
            }
        });
        startSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            Thread search = new Thread(() -> {
                {
                    Matcher matcher = searchField.getMatches(searchField.getText(), textArea.getText());
                    matches.clear();
                    index.clear();
                    if (RegExCheckbox.isSelected()){
                        while (matcher.find()) {
                            matches.add(matcher.group());
                            index.add(matcher.start());
                        }
                    }else {
                        while (matcher.find()&& matcher.group().equals(matcher.pattern().pattern())) {
                            matches.add(matcher.group());
                            index.add(matcher.start());
                        }
                    }
                }
                if (index.listIterator().hasNext()){
                    index.listIterator().next();
                    textArea.setCaretPosition(index.get(0)+matches.get(0).length());
                    textArea.select(index.get(0),index.get(0)+matches.get(0).length());
                    textArea.grabFocus();

                }
            });
            search.start();
            try {
                search.join();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            }
        });
        nextSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ListIterator<Integer> iterator = index.listIterator();
                int iterIndex=0;
                if (iterator.hasNext()) {
                    iterator.next();
                     iterIndex = iterator.nextIndex();
                }else {
                    while (iterator.hasPrevious()){
                        iterIndex=iterator.previousIndex();
                        iterator.previous();
                    }
                }
                textArea.setCaretPosition(index.get(iterIndex)+matches.get(iterIndex).length());
                textArea.select(index.get(iterIndex),index.get(iterIndex)+matches.get(iterIndex).length());
                textArea.grabFocus();
            }
        });
        prevSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ListIterator<Integer> iterator = index.listIterator();
                int iterIndex=0;
                if (iterator.hasPrevious()) {
                    iterator.previous();
                    iterIndex = iterator.previousIndex();
                }else {
                    while (iterator.hasNext()){
                        iterIndex=iterator.nextIndex();
                        iterator.next();
                    }
                }
                textArea.setCaretPosition(index.get(iterIndex)+matches.get(iterIndex).length());
                textArea.select(index.get(iterIndex),index.get(iterIndex)+matches.get(iterIndex).length());
                textArea.grabFocus();
                System.out.println(textArea.getCaretPosition());
            }
        });
        menuItemStartSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Thread search = new Thread(() -> {
                    {
                        Matcher matcher = searchField.getMatches(searchField.getText(), textArea.getText());
                        matches.clear();
                        index.clear();
                        if (RegExCheckbox.isSelected()){
                            while (matcher.find()) {
                                matches.add(matcher.group());
                                index.add(matcher.start());
                            }
                        }else {
                            while (matcher.find()&& matcher.group().equals(matcher.pattern().pattern())) {
                                matches.add(matcher.group());
                                index.add(matcher.start());
                            }
                        }
                    }
                    if (index.listIterator().hasNext()){
                        index.listIterator().next();
                        textArea.setCaretPosition(index.get(0)+matches.get(0).length());
                        textArea.select(index.get(0),index.get(0)+matches.get(0).length());
                        textArea.grabFocus();

                    }
                });
                search.start();
                try {
                    search.join();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
        menuItemNextSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ListIterator<Integer> iterator = index.listIterator();
                int iterIndex=0;
                if (iterator.hasNext()) {
                    iterator.next();
                    iterIndex = iterator.nextIndex();
                }else {
                    while (iterator.hasPrevious()){
                        iterIndex=iterator.previousIndex();
                        iterator.previous();
                    }
                }
                textArea.setCaretPosition(index.get(iterIndex)+matches.get(iterIndex).length());
                textArea.select(index.get(iterIndex),index.get(iterIndex)+matches.get(iterIndex).length());
                textArea.grabFocus();
            }
        });
        menuItemPrevSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ListIterator<Integer> iterator = index.listIterator();
                int iterIndex=0;
                if (iterator.hasPrevious()) {
                    iterator.previous();
                    iterIndex = iterator.previousIndex();
                }else {
                    while (iterator.hasNext()){
                        iterIndex=iterator.nextIndex();
                        iterator.next();
                    }
                }
                textArea.setCaretPosition(index.get(iterIndex)+matches.get(iterIndex).length());
                textArea.select(index.get(iterIndex),index.get(iterIndex)+matches.get(iterIndex).length());
                textArea.grabFocus();
            }
        });
        menuItemUseRegex.addActionListener(actionEvent -> RegExCheckbox.doClick());

        setVisible(true);
    }
    public void openFile(){
        int ret = fileChooser.showOpenDialog(null);
        if (ret==JFileChooser.APPROVE_OPTION) {
            try {
                file= fileChooser.getSelectedFile();
                setTitle(TITLE+"  -  "+file.getName());
                textArea.setText(readFileAsString(file.getAbsolutePath()));
            } catch (IOException e) {
                textArea.setText("");
                e.printStackTrace();
            }
        }
            TextEditor.this.revalidate();
    }
    public void saveFile(){
        int ret = fileChooser.showSaveDialog(null);
        if (ret==JFileChooser.APPROVE_OPTION) {
            file= fileChooser.getSelectedFile();
            setTitle(TITLE+"  -  "+file.getName());
            writeFile(file.getAbsolutePath(), textArea.getText());
        }
    }
    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
    public static void writeFile(String fileName,String string) {
        File file = new File(fileName);
        try(PrintWriter printWriter = new PrintWriter(file)){
            printWriter.print(string);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
