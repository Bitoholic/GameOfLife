package model;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * This class controls all the sounds in the game.
 * 
 * @author Ali Arfan
 * @author Knut Erlend Bratteng Knudsen
 * @author Stian Tornholm Grimsgaard
 *
 */

public class Sound
{
    
    public static void play(String url) {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        ex.printStackTrace();
	    }
	    System.gc();
	}
}
