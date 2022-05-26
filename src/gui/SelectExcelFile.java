package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author jad nasser
 */
public class SelectExcelFile extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final JButton back;
    private final JPanel mainPanel;

    protected SelectExcelFile(JPanel mainPanel, JButton back) {
        this.back = back;
        this.mainPanel = mainPanel;
        createPage();
    }

    //this method will be executed when the browse button is clicked
    private void handleBrowseClick() {
        //create file chooser
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel *.xlsx", "xlsx", "XLSX");
        fileChooser.setFileFilter(filter);
        //show file chooser
        int result = fileChooser.showOpenDialog(fileChooser);
        if (result == JFileChooser.APPROVE_OPTION) {
            if (fileChooser.getSelectedFile() != null) {
                String excelFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                int filePathLength = excelFilePath.length();
                String fileExtention = excelFilePath.substring(filePathLength - 4, filePathLength);
                if (fileExtention.equals("xlsx")) {
                    //removing the current panel and replace it with CreateQueriesFile panel
                    JPanel createQueriesFile = new CreateQueriesFile(mainPanel, back, excelFilePath);
                    mainPanel.remove(this);
                    mainPanel.add(createQueriesFile);
                    mainPanel.revalidate();
                    mainPanel.repaint();
                    //enable back button
                    back.setEnabled(true);
                }
            }
        }
    }

    private void createPage() {
        this.setLayout(new BorderLayout());

        //creating the browse button that opens a file selector when clicked
        JButton browse = new JButton("Browse");
        //adding action listener to browse button
        browse.addActionListener(e -> handleBrowseClick());

        //creating the bottom panel of this page
        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        this.add(bottom, BorderLayout.SOUTH);
        //adding the browse button to the bottom panel
        bottom.add(browse);

        // creating select file label and adding it to this page
        JLabel selectFileLabel = new JLabel("Select an Excel file", SwingConstants.CENTER);
        this.add(selectFileLabel, BorderLayout.CENTER);
    }

}
