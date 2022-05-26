package gui;

import core.Engine;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author jad nasser
 */
public class CreateQueriesFile extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String excelFilePath;
    private final JPanel mainPanel;
    private final JButton back;

    protected CreateQueriesFile(JPanel mainPanel, JButton back, String excelFilePath) {
        this.excelFilePath = excelFilePath;
        this.mainPanel = mainPanel;
        this.back = back;
        createPage();
    }

    //this method will be executed when the browse button is clicked
    private void handleBrowseClick() {
        //Opening file chooser
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(fileChooser);
        if (result == JFileChooser.APPROVE_OPTION) {
            //getting the path of the destination file
            String resultFilePath = fileChooser.getSelectedFile().getAbsolutePath();
            //convert the data from the excel file and show the result to the user in
            //the message dialog box
            JOptionPane messageBox = new JOptionPane();
            Engine engine = new Engine();
            JOptionPane.showMessageDialog(messageBox, engine.convert(excelFilePath, resultFilePath));
        }
    }

    //this method will be executed when the back button is clicked
    private void handleBackClick() {
        //removing the current panel and add SelectExcelFile panel
        JPanel selectExcelFile = new SelectExcelFile(mainPanel, back);
        mainPanel.remove(this);
        mainPanel.add(selectExcelFile);
        mainPanel.revalidate();
        mainPanel.repaint();
        //removing the action listener from the back button
        for (ActionListener al : back.getActionListeners()) {
            back.removeActionListener(al);
        }
        //disable the back button
        back.setEnabled(false);
    }

    private void createPage() {
        this.setLayout(new BorderLayout(10, 10));

        //create browse button that will open file chooser after clicking
        JButton browse = new JButton("Browse");
        //adding action listener to browse button
        browse.addActionListener(e -> handleBrowseClick());

        //adding action listener to the back button
        back.addActionListener(e -> handleBackClick());

        //creating the bottom panel of this page
        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        this.add(bottom, BorderLayout.SOUTH);
        //adding the browse button to the bottom panel
        bottom.add(browse);

        // creating save file label and adding it to this page
        JLabel saveFileLabel = new JLabel("Where you want to save the result file?",
                SwingConstants.CENTER);
        this.add(saveFileLabel, BorderLayout.CENTER);
    }

}
