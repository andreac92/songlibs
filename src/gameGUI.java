
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTextField;
import java.awt.Dimension;

public class gameGUI {

	private JFrame frmSonglibs;
	private JPanel cards;
	private JPanel startPage;
	private JPanel madlibPage;
	private JPanel finishedPage;
	private gameLogic chosenSong;
	private JComboBox comboBox;
	private JScrollPane scroller;
	private HashMap<JTextField, String> toGet;
	
	/**
	 * Creates the game based on song choice; creates end of game
	 * through the "submit" action
	 */
	public void game(){
		int i = makePlayScreen();
		makeSubmit(i);
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gameGUI window = new gameGUI();
					window.frmSonglibs.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public gameGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		makeStartPage();
		makestartPageText();
		makeSongOptions();
		makeRandomButton();
		makeMadLibPage();
		makeFinishedPage();
	}
	
	/**
	 * 1. Get values from hashmap (keys are specific word, ie NOUN1, NOUN2; 
	 *     value is descriptor ie a NOUN)
	 * 2. Put these values in a label, place next to text field
	 * 3. Put text field and key from previous hashmap in a new hashmap
	 */
private int makePlayScreen() {
	toGet = new HashMap<JTextField, String>();
	int i=5;
	List<Object> valuesList = new ArrayList<Object>(chosenSong.replaceWords.keySet());
	Collections.shuffle(valuesList);
	for (Object word : valuesList) {
		JLabel lblNewLabel_1 = new JLabel(chosenSong.replaceWords.get(word));
		lblNewLabel_1.setBounds(5, i, 161, 16);
		madlibPage.add(lblNewLabel_1);
		madlibPage.revalidate();
		madlibPage.repaint();
		JTextField txt1 = new JTextField();
		txt1.setBounds(180, i, 130, 30);
		toGet.put(txt1, (String)word);
		madlibPage.add(txt1);
		madlibPage.revalidate();
		madlibPage.repaint();
		txt1.setColumns(10);
		i+=30;
	}
	return i;
}

/**
* Create the submit button
*/
private void makeSubmit(int i){
	JButton jb = new JButton("Submit");
	jb.addActionListener(new submitActions());
	jb.setBounds(5, i, 70, 16);
	madlibPage.add(jb);
	madlibPage.setPreferredSize(new Dimension(400, i+40));
	madlibPage.revalidate();
	madlibPage.repaint();
}

/**
* Action performed on pressing submit button:
* 1. Get word typed into text field, place into song
* 2. Display final song
* 3. Display option to redo the song, and option to do another song
*/
public class submitActions implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	       for (JTextField txt : toGet.keySet()) {
		   String userWord = txt.getText();
		   String word = toGet.get(txt);
		   chosenSong.replaceWord(word, userWord);
		}
		showSong();
		restartAction();
		redoAction();
		finishedPage.revalidate();
		finishedPage.repaint();
		CardLayout cl = (CardLayout)(cards.getLayout());
		cl.next(cards);
	}
}
/**
* Display the finished song
*/
private void showSong() {
	JLabel madlibsongName = new JLabel(chosenSong.songName);
	madlibsongName.setBounds(155, 6, 400, 20);
	JTextArea finishedmadlibsong = new JTextArea(chosenSong.song);
	finishedmadlibsong.setWrapStyleWord(true);
	finishedmadlibsong.setEditable(false);
	finishedmadlibsong.setLineWrap(true);
	finishedmadlibsong.setOpaque(false);
	finishedmadlibsong.setBounds(25, 31, 390, 200);
	JScrollPane scrollPane = new JScrollPane(finishedmadlibsong);
	scrollPane.setBounds(25,31,420,200);
	scrollPane.setOpaque(false);
	scrollPane.getViewport().setOpaque(false);
	scrollPane.setBorder(BorderFactory.createEmptyBorder());
	finishedPage.add(madlibsongName);
	finishedPage.add(scrollPane);
}
/**
* Create the restart button, and its action to clear the song, song panel and
* move to the start page panel.
*/
private void restartAction() {
	JButton restart = new JButton("Choose another song");
	restart.setBounds(80, 250, 160, 20);
	restart.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	       madlibPage.removeAll();
	       madlibPage.revalidate();
	       madlibPage.repaint();
	       scroller.revalidate();
	       scroller.repaint();
	       finishedPage.removeAll();
	       finishedPage.revalidate();
	       finishedPage.repaint();
	       CardLayout cl = (CardLayout)(cards.getLayout());
	       comboBox.setSelectedIndex(0);
	       cl.show(cards, "home");
	       }
	});
	finishedPage.add(restart);
}
/**
* Create the redo button and its action to move back to the song panel
*/
private void redoAction() {
	JButton redo = new JButton("Redo song");
	redo.setBounds(250, 250, 100, 20);
	redo.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	       chosenSong.reset();
	       madlibPage.removeAll();
	       madlibPage.revalidate();
	       madlibPage.repaint();
	       scroller.revalidate();
	       scroller.repaint();
	       finishedPage.removeAll();
	       finishedPage.revalidate();
	       finishedPage.repaint();
	       CardLayout cl = (CardLayout)(cards.getLayout());
	       comboBox.setSelectedIndex(0);
	       game();
	       cl.show(cards, "madlibsong");
	       }
	});
	finishedPage.add(redo);
}
/**
* Create the panel for the start page where users choose their song
*/
private void makeStartPage(){
	frmSonglibs = new JFrame();
	frmSonglibs.setTitle("SongLibs");
	frmSonglibs.setBounds(100, 100, 450, 315);
	frmSonglibs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frmSonglibs.getContentPane().setLayout(new CardLayout(0, 0));
	cards = (JPanel) frmSonglibs.getContentPane();
	startPage = new JPanel();
	frmSonglibs.getContentPane().add(startPage, "home");
	startPage.setLayout(null);
}
/**
* Create the title and instruction text for the start page
*/
private void makestartPageText() {
	JLabel lblmadlibsonglibs = new JLabel("SONGLIBS");
	lblmadlibsonglibs.setFont(new Font("Bookman Old Style", Font.PLAIN, 37));
	lblmadlibsonglibs.setBounds(115, 6, 200, 44);
	startPage.add(lblmadlibsonglibs);
	JLabel lblNewLabel = new JLabel("<html>Your classic madlibs game -- with a twist! Fill in the<br>words to your favorite songs and enjoy the hilarity that ensues. Choose a song from the list, or have one chosen for you by clicking the random button.");
	lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
	lblNewLabel.setBounds(48, 63, 374, 73);
	startPage.add(lblNewLabel);
	JLabel lblOr = new JLabel("- OR -");
	lblOr.setBounds(180, 187, 61, 16);
	startPage.add(lblOr);
}
/**
* Create the drop-down menu for the song options on the start page
*/
private void makeSongOptions() {
	chosenSong = new gameLogic();
	String[] options = chosenSong.getOptions();
	comboBox = new JComboBox(options);
    comboBox.setEditable(false);
	comboBox.setBounds(75, 148, 275, 27);
	comboBox.addActionListener(new ActionListener() {
	  @Override
	  public void actionPerformed(ActionEvent e) {
	    	if (((String)comboBox.getSelectedItem()).equals("")){
	    	   return;
	    	}
	        chosenSong.choose((String)comboBox.getSelectedItem());
	        chosenSong.initializeLib();
	        game();
	        CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, "madlibsong");
	        }
	    });
	startPage.add(comboBox);
}
/**
* Create the button for choosing a random song
*/
private void makeRandomButton() {
	JButton btnRandom = new JButton("Random");
	btnRandom.setBounds(143, 215, 117, 29);
	btnRandom.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		       chosenSong.randomize();
		       chosenSong.initializeLib();	            
		       game();
	           CardLayout cl = (CardLayout)(cards.getLayout());
	           cl.show(cards, "madlibsong");
		 }
	});
	startPage.add(btnRandom);
}
/**
* Create the madlib panel
*/
private void makeMadLibPage() {
		JPanel panel2 = new JPanel();
		scroller = new JScrollPane(panel2);
		madlibPage = panel2;
		frmSonglibs.setPreferredSize(new Dimension(450, 200));
		frmSonglibs.getContentPane().add(scroller, "madlibsong");
		panel2.setLayout(null);
}
/**
* Create the finished song panel
*/
private void makeFinishedPage() {
	    finishedPage = new JPanel();
	    frmSonglibs.getContentPane().add(finishedPage, "result");
	    finishedPage.setLayout(null);
}

}
