package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextEditor extends JFrame {
    JPanel mainPanel = new JPanel();
    JButton buttonLoad = new JButton("Open");
    JButton buttonSave = new JButton("Save");
    JTextField fieldFileName = new JTextField("",5);
    JTextArea TextArea = new JTextArea(5,4);
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem menuItemOpen = new JMenuItem("Open");
    JMenuItem menuItemSave = new JMenuItem("Save");
    JMenuItem menuItemExit = new JMenuItem("Exit");

    JTextField SearchField = new JTextField("",5);
    JButton startSearchButton = new JButton("Start Search");
    JButton prevSearchButton = new JButton("Previous Match Search");
    JButton nextSearchButton = new JButton("Next Match Search");
    JCheckBox RegExCheckbox = new JCheckBox("Use checkbox");
    JFileChooser FileChooser = new JFileChooser();
    JMenu searchMenu = new JMenu("Search");
    JMenuItem menuItemStartSearch = new JMenuItem("Start Search");
    JMenuItem menuItemPrevSearch = new JMenuItem("Previous Search");
    JMenuItem menuItemNextSearch = new JMenuItem("Next Search");
    JMenuItem menuItemUseRegex = new JMenuItem("Use Regex");




    public TextEditor() {
        super("The first stage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setContentPane(mainPanel);
        JScrollPane scrollPane = new JScrollPane(TextArea);

        TextArea.setName("TextArea");
        SearchField.setName("SearchField");

        fieldFileName.setName("FilenameField");

        buttonSave.setName("SaveButton");
        buttonLoad.setName("OpenButton");
        startSearchButton.setName("StartSearchButton");
        prevSearchButton.setName("PreviousMatchButton");
        nextSearchButton.setName("NextMatchButton");
        RegExCheckbox.setName("UseRegExCheckbox");
        FileChooser.setName("FileChooser");
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
        mainPanel.add(fieldFileName);
        mainPanel.add(buttonSave);
        mainPanel.add(buttonLoad);
        mainPanel.add(scrollPane);


        scrollPane.setMinimumSize(new Dimension(200,100));
        scrollPane.setPreferredSize(new Dimension(200,100));
        buttonLoad.addActionListener(actionEvent -> {
            openFile();
        });
        buttonSave.addActionListener(actionEvent -> {
            saveFile();
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

        setVisible(true);
    }
    public void openFile(){
        try {
            TextArea.setText(readFileAsString(fieldFileName.getText()));
        } catch (IOException e) {
            TextArea.setText("");
            e.printStackTrace();
        }
        TextEditor.this.revalidate();
    }
    public void saveFile(){
        writeFile(fieldFileName.getText(),TextArea.getText());
    }
    public static String readFileAsString(String fileName) throws IOException, IOException {
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
