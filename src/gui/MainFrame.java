package gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author jad nasser
 */
public class MainFrame extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainFrame() {

        //creating the main panel of the GUI
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BorderLayout(10, 10));
        //adding the main panel to the main frame
        this.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        //creating the back button
        JButton back = new JButton("Back");
        back.setEnabled(false);

        //creating the bottom panel of the main panel of the main frame
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout(10, 10));
        mainPanel.add(bottom, BorderLayout.SOUTH);
        //adding the back button to the bottom panel
        bottom.add(back, BorderLayout.WEST);

        //creating the title label of the app and adding it to the top of the main panel
        JLabel appTitle = new JLabel("From Excel to Relational Database Converter",
                SwingConstants.CENTER);
        mainPanel.add(appTitle, BorderLayout.NORTH);

        //adding SelectExcelFile panel to the main panel
        JPanel selectExcelFile = new SelectExcelFile(mainPanel, back);
        mainPanel.add(selectExcelFile, BorderLayout.CENTER);

        this.setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

}
