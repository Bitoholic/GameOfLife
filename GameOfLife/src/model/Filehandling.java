package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the file opened  and loaded by the Filechooser in JavaFX. The read data is added into an ArrayList by the type
 * String, and the  String ArrayList is used to create an patternFormat object. 
 * 
 * @see javafx.stage.FileChooser
 * @see PatternFormat 
 * 
 * @author Ali Arfan
 * @author Kent Erlend Bratteng Knudsen
 * @author Stian Tornholm Grimsgaard
 */

public class Filehandling {
	
	private List<String> rleData = new ArrayList<>(); 
	PatternFormat pattern = null;  
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getFilePattern</i></b>
	 * <pre>{@code}public {@link PatternFormat} getFilePattern()</pre>
	 * <p>Returns the PaternFormat object, where the read data from the RLE file is set to pattern.</p>
	 * @return pattern - The RLE data read for the opened file.
	 * </blockquote>
	 * 
	 */
	
	public PatternFormat getFilePattern() {
		return pattern; 
	}
	
	
	/**
	 * <blockquote>
	 * <b><i>readGameBoard</i></b>
	 * <pre>{@code}public void readGameBoard({@link BufferedReader} br)</pre>
	 * <p>Reads each line in the file, and adds it to the String ArrayList, the data read is added at the end of the array.</p>
	 * 
	 * @param br - Takes a BufferedReader as an parameter
	 * @throws IOException - if the gameboard cannot be read or file does not exists.
	 * @throws PatternFormatException - if there was an error loading the RLE file.
	 * @throws MalformedURLException -  when a malformed URL has occurred.
	 * @throws MalformedInputException - when an input byte sequence is not legal for given charset.
	 * </blockquote>
	 *
	 */
	public void readGameBoard(BufferedReader br) throws IOException, PatternFormatException, MalformedURLException, MalformedInputException {
		String buffer;
		
		try {
			while((buffer = br.readLine()) != null) {
				rleData.add(buffer);
			}
		} finally {
			br.close();
		}
		
		pattern = new PatternFormat(rleData);
	}
	
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>readGameBoardFromDisk</i></b>
	 * <pre>{@code}public void readGameBoardFromDisk({@link Path} path)</pre>
	 * <p>Opens and read the RLE file from the disk.</p>
	 * 
	 * @param path - Path of the file
	 * @throws IOException - if the gameboard cannot be read or file does not exists.
	 * @throws PatternFormatException - if there was an error loading the RLE file.
	 * @throws NoSuchFileException -  thrown when an attempt is made to access a file that does not exist.
	 * @throws MalformedInputException - when an input byte sequence is not legal for given charset.
	 * </blockquote>
	 * 
	 */
	public void readGameBoardFromDisk(Path path) throws IOException, PatternFormatException, NoSuchFileException, MalformedInputException  {
		readGameBoard(Files.newBufferedReader(path));
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>readGameBoardFromURL</i></b>
	 * <pre>{@code}public void readGameBoardFromURL({@link String} url)</pre>
	 * <p>Opens and reads the URL of the RLE file.</p>
	 * 
	 * @param url - Internet URL containing the URL file 
	 * @throws IOException -  I/O exception of some sort has occurred.
	 * @throws PatternFormatException - thrown when the RLE file is incorret.
	 * @throws MalformedURLException - no legal protocol could be found in a specification string or the string could not be parsed.
	 * </blockquote>
	 * 
	 */
	
	public void readGameBoardFromURL(String url) throws IOException, PatternFormatException, MalformedURLException {
		URL destination = new URL(url); 
		URLConnection conn = destination.openConnection(); 
		readGameBoard(new BufferedReader(new InputStreamReader(conn.getInputStream())));
	} 
}
