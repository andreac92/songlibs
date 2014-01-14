
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

public class gameLogic {
	private BufferedReader reader;
	private Properties songChoices;
	private String origSong;
	String songName;
	String song;
	HashMap<String, String> replaceWords;
	
	/**
	 * Initialize values
	 */
	public gameLogic(){
		songChoices = new Properties();
		song ="";
		load();
	}
	/**
	 * Load properties file of song options
	 */
	public void load(){
		try {
			songChoices.load(new FileInputStream("songChoices.properties"));
		} catch (IOException e) {
			create();
		}
	}
	/**
	 * For testing purposes -- Hardcoded test songs
	 */
	public void createTest() {
		try {
			songChoices.setProperty("Mary Had A Little Lamb", "adlib1.txt");
			songChoices.setProperty("Birthday Song", "birthdaysong.txt");
			songChoices.setProperty("Call Me Maybe", "callmemaybe.txt");
			songChoices.store(new FileOutputStream("songChoices.properties"), null);
		} catch (IOException e){
			System.err.print("Error");
		}
	}
	/**
	 * Create properties file if it doesn't exist
	 */
	public void create(){
		String cd = System.getProperty("user.dir").toString();
		cd+="/songs";
		final File folder = new File(cd);
		File[] songs = folder.listFiles();
		try {
			for (File file : songs) {
				if (file.getName().startsWith(".")){
					continue;
				}
			songChoices.setProperty(file.getName().substring(0, file.getName().length() - 4), file.toString());
				}
		} catch (NullPointerException e) {
			
		}
	}
	
	/**
	 * For testing purposes -- 1. list the options from properties file
	 * 2. Choose song option through command line
	 */
	public void listOptions(){
		Enumeration<?> list = songChoices.propertyNames();
		System.out.println("Type in the name of a SONGLIB song from the following:");
		while (list.hasMoreElements()){
			System.out.println(list.nextElement());
		}
		System.out.println();
		Scanner in = new Scanner(System.in);
		String choice = null;
		while (choice == null) {
			System.out.print("song: ");
			choice = in.nextLine();
			songName = choice;
			choice = songChoices.getProperty(choice);
			if (choice != null){
				try {
					reader = new BufferedReader(new FileReader(choice));
				} catch (Exception e){
				}
			} else {
				System.out.println("Incorrect SONGLIB name!");
			}
		}
	}
	/**
	 * Return a string array of song options; first option left blank
	 */
	public String[] getOptions(){
		Enumeration<?> list = songChoices.propertyNames();
		String[] toRtn = new String[songChoices.size()+1];
		int i = 1;
		toRtn[0] = "";
		while (list.hasMoreElements()){
			toRtn[i] = list.nextElement().toString();
			i++;
		}
		return toRtn;
	}
	/**
	 * Select the song choice's text file from the property file
	 */
	public void choose(String choice){
		songName = choice;
		choice = songChoices.getProperty(choice);
		try {
			reader = new BufferedReader(new FileReader(choice));
		} catch (Exception e){
		}
	}
	/**
	 * Read song file. Put words to be replaced in hashmap.
	 */
	public void initializeLib(){
		try {
			String line;
			song="";
			replaceWords = new HashMap<String, String>();
			while (!(line = reader.readLine()).equals("***")){
				int space = line.indexOf(" ");
				replaceWords.put(line.substring(0, space), line.substring(space+1));
			}
			while ((line = reader.readLine()) != null){
				song += line + "\n";
			}
			origSong = song;
		} catch (Exception e){
			
		}
	}
	/**
	 * Choose a random song
	 */
	public void randomize(){
		Enumeration<?> list = songChoices.propertyNames();
		Random rn = new Random();
		int i = rn.nextInt(songChoices.size());
		String choice="";
		for (int z=0; z<=i; z++){
			choice = list.nextElement().toString();
		}
		choose(choice);
	}
	/**
	 * Reset the song to its original state
	 */
	public void reset(){
		song = origSong;
	}
	/**
	 * For testing purposes -- 1. Get user-entered words
	 * 2. Place user-entered words into song
	 */
	public void getUserWords(){
		System.out.println();
		System.out.println("Enter a word for each type listed");
		Scanner in = new Scanner(System.in);
		String userWord;
		for (String word : replaceWords.keySet()) {
			System.out.print(replaceWords.get(word) + ": ");
			userWord = in.nextLine();
			song = song.replaceAll(word, userWord);
		}
		
	}
	/**
	 * Replace words to be replaced with user-entered word
	 */
	public void replaceWord(String word, String userWord){
		song = song.replaceAll(word, userWord);
	}
	/**
	 * For testing purposes -- Print song with user substitutions
	 */
	public void printResult(){
		System.out.println();
		System.out.println(songName);
		System.out.println(song);
	}
}
