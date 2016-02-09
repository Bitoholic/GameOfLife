package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Alert.AlertType;
import view.Viewer;

/**
 * This class manages the rules of Conway's Game of Life.  GameRules can  load, edit and 
 * save rules.  The rules are saved and taken in as  String objects, where the format of the String
 * is the same format for Rule Strings in RLE-files.  The rules are validated and checked if they are correct,
 * if they are incorrect, the user is informed.
 * 
 * All the rules are added into a RuleCollection arrayList, where each rule has is its own index.
 * 
 * @author Ali Arfan
 * @author Kent Erlend Bratteng Knudsen
 * @author Stian Tornholm Grimsgaard
 * 
 */

public class GameRules {
	
	private final String DATAFILE = "rules.bin";
	
	private static GameRules reference; 
	private List<Rule> rulesCollection = new ArrayList<Rule>(); 
	private boolean[] rulesBirth; 
	private boolean[] rulesSurvive;     
	private int index = 0;
	
	/* 
	 * RuleFormat is the format of how  the RuleString can be, if its  an empty string its 
	 * INVALID,  Survive_THEN_BIRTH takes first a S-argument(survives), and then B-arguemnt(birth).
	 * 
	 * RuelFormat is used to see if  the saved Rule is valid or not. 
	 *  
	 *
	 */
	public static enum RuleFormat {
		SURVIVE_THEN_BIRTH	("S([0-8]*)/B([0-8]*)"),  // 
		BIRTH_THEN_SURVIVE	("B([0-8]*)/S([0-8]*)"),
		LEGACY				("([0-8]*)/([0-8]*)"),
		INVALID 			("");
		
		private final String ruleFormatString;
		
		private RuleFormat(String ruleFormatString) {
			this.ruleFormatString = ruleFormatString;
		}
		
		public String ruleFormatString() {
			return ruleFormatString;
		}
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>GameRules</i></b>
	 * <pre>{@code}private GameRules()</pre>
	 * <p>RulesBirth and RuleSurvive array is created with 9  empty elements.</p>
	 * <p>The rules are Set to the default rules for Conway's Game of Life(s23/b3), if a invalid rule is selected, or no rule is selected at all. </p>
	 * </blockquote>
	 * 
	 */
	
	private GameRules() {
	
		
		rulesBirth = new boolean[9];
		rulesSurvive = new boolean[9];
		
		setRules("s23/b3");
		
		loadRulesFile();
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getInstance</i></b>
	 * <pre>{@code}public static {@link GameRules} getInstance()</pre>
	 * <p>If the reference to a gameRule is empty, a reference to the class GameRules is created, and returns
	 *  its instance.</p>
	 * @return - Reference to the GameRule object.  
	 * </blockquote>
	 * 
	 */
	
	public static GameRules getInstance() {
		if(reference == null) {
			reference = new GameRules();
		}
		
		return reference;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>loadRulesFile</i></b>
	 * <pre>{@code}public void loadRulesFile()</pre>
	 * <p>The rules are loaded from   Rules file , which has the extension "*.bin".</p>
	 * <p>The title, name, RuleString and description of the rule is read, and added into the list of existing rules. </p>
	 * @see IOException - Throws an error if the "rules.ser" file can not be loaded.
	 * </blockquote>
	 * 
	 */
	
	public void loadRulesFile() {
		Path path = Paths.get(DATAFILE);
		
		/*
		 *  If the file doesn't exists, it might be because it's the first
		 *  time the program is run. We therefore create a new file to hold
		 *  the rules.
		 */
		
		if(!Files.exists(path)) {
			try(ObjectOutputStream fo = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
				Rule rule;
				
				rule = new Rule();
				rule.setRuleIndex(0);
				rule.setRuleName("Conway's Life");
				rule.setRuleString("s23/b3");
				rule.setRuleDescription("A chaotic rule that is by far the most well-known and well-studied. It exhibits highly complex behavior.");
				
				rulesCollection.add(rule);
				
				rule = new Rule();
				rule.setRuleIndex(1);
				rule.setRuleName("Replicator");
				rule.setRuleString("s1357/b1357");
				rule.setRuleDescription("A rule in which every pattern is a replicator.");
				
				rulesCollection.add(rule);
				
				for(Rule o : rulesCollection) {
					fo.writeInt(o.getRuleIndex());
					fo.writeUTF(o.getRuleName());
					fo.writeUTF(o.getRuleString());
					fo.writeUTF(o.getRuleDescription());
				}
				
				fo.close();
			} catch(IOException e) {
				Viewer.popupBox( 
						AlertType.ERROR,
						"IOException error",
						"Could not create file " + DATAFILE,
						e.getMessage());
			}
		} else {
			try(ObjectInputStream fo = new ObjectInputStream(new FileInputStream(path.toFile()))) {
				Rule rule;
				
				while(fo.available() > 0) {
					rule = new Rule();
					rule.setRuleIndex(fo.readInt());
					rule.setRuleName(fo.readUTF());
					rule.setRuleString(fo.readUTF());
					rule.setRuleDescription(fo.readUTF());
					
					rulesCollection.add(rule);
				}

				fo.close();
			} catch(IOException e) {
				Viewer.popupBox( 
						AlertType.ERROR,
						"IOException error",
						"Could not correctly load the file " + DATAFILE,
						e.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>saveRulesFile</i></b>
	 * <pre>{@code}public void saveRulesFile()</pre>
	 * <p>Saves the rules in the RulesCollection array to the Rule file with extension ".ser".</p>
	 * @see IOException 
	 * </blockquote>
	 * 
	 */
	public void saveRulesFile() {
		Path path = Paths.get(DATAFILE);
		
		try(ObjectOutputStream fo = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
			for(Rule o : rulesCollection) {
				fo.writeInt(o.getRuleIndex());
				fo.writeUTF(o.getRuleName());
				fo.writeUTF(o.getRuleString());
				fo.writeUTF(o.getRuleDescription());
			}
			
			fo.close();
		} catch(IOException e) {
			Viewer.popupBox( 
					AlertType.ERROR,
					"IOException error",
					"Could not create file " + DATAFILE,
					e.getMessage());
		}
	}
	
	/**
	 *¨
	 * <blockquote>
	 * <b><i>setNextRulesCollection</i></b>
	 * <pre>{@code}public void setNextRulesCollection(int index)</pre>
	 * <p>The next Rule, is found and set for the Rule array. This method is to calculate the next rule
	 *  after the current rule.</p>
	 * @param index -  index of the current rule position in the array.  
	 * </blockquote>
	 * 
	 */
	
	public void setNextRulesCollection(int index) {
		if(index < 0) index = 0;
		if(index > rulesCollection.size()) index = rulesCollection.size() - 1;
				
		this.index = index;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getNextRulesCollection</i></b>
	 * <pre>{@code}public {@link Rule} getNextRulesCollection()</pre>
	 * <p>Uses the index of the valid rule, and finds the next rule in-line and returns it. </p>
	 * @return - Returns the rule which is after the current rule in the array.  
	 * </blockquote>
	 */
	
	public Rule getNextRulesCollection() {
		// index used is the index of the current rule. 

		return rulesCollection.get(index++); 	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>hasNextRulesCollection</i></b>
	 * <pre>{@code}public boolean hasNextRulesCollection()</pre>
	 * <p>Checks if the next rules exists or not, returns false if there is no next rule, and it returns true 
	 * if a next rule exists.</p>
	 * @return - True or false, according to if the next rules exists or not. 
	 * </blockquote>
	 */
	
	public boolean hasNextRulesCollection() {
		return (index < rulesCollection.size());
	}
	
	/**
	 * <blockquote>
	 * <b><i>getNextRulesCollectionIndex</i></b>
	 * <pre>{@code}public {@link Integer} getNextRulesCollectionIndex({@link Rule} rule)</pre>
	 * <p>Returns the index of a given rule in the Rule Collection Array.</p>
	 * 
	 * @param rule - Takes a rule as argument. 
	 * @return - The index is returned. 
	 *</blockquote>
	 *
	 */
	
	public int getNextRulesCollectionIndex(Rule rule) {
		return rulesCollection.indexOf(rule);
	}
	
	/**
	 * <blockquote>
	 * <b><i>addRuleToCollection</i></b>
	 * <pre>{@code}public void addRuleToCollection({@link String} ruleName, String ruleString, String ruleDescription)</pre>
	 * <p>Adds a rule to the Rule Array, the ruleString has to be a Birth/Survival or Survival/Birth ruleString.</p>
	 * 
	 * @param ruleName - the name of the rule 
	 * @param ruleString - the RuleString- the survival and birth rate. 
	 * @param ruleDescription - the description of the rule. 
	 * @throws Exception
	 * </blockquote>
	 */
	public void addRuleToCollection(String ruleName, String ruleString, String ruleDescription) throws Exception {
		Rule rule = new Rule();
		
		rule.setRuleName(ruleName);
		rule.setRuleString(ruleString);
		rule.setRuleDescription(ruleDescription);
		rule.setRuleIndex(rulesCollection.size());
		
		rulesCollection.add(rule);
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>checkRules</i></b>
	 * <pre>{@code}public boolean checkRules(boolean cellState, int neighbours)</pre>
	 * <p>Checks if a cell should be alive or dead according to defined rules
	 * and returns its new state.</p>
	 * 
	 * @param cellState - boolean value defining a cell's state.
	 * @param neighbours - byte value between 0..8 holding the count of alive neighbors next to a cell.
	 * @return - returns a boolean value of the cell's new state set by the rules.
	 * @throws IllegalArgumentException - If the neighbors is not between 0 and 8, an exception is thrown.
	 * </blockquote>
	 * 
	 */
	public boolean checkRules(boolean cellState, int neighbours) throws IllegalArgumentException {
		if(neighbours < 0 || neighbours > 9) {
			throw new IllegalArgumentException("Neighbors must between 0 and 8.");
		}
		
		// If cell is alive, check with rules for surviving.
		// If cell is dead, check with rules for birth.
		return (cellState == true ? rulesSurvive[neighbours] : rulesBirth[neighbours]);
	}
	
	/**
	 * <blockquote>
	 * <b><i>ruleStringFormat</i></b>
	 * <pre>{@code}public static {@link RuleFormat} ruleStringFormat({@link String} ruleString)</pre>
	 * <p>Trims the RuleString for white space, and filters the  birth and survival values.</p>
	 * @param ruleString - Trims the rule String.
	 * @return RuleFormatValues - returns the survival and birth values of the RuleString
	 * </blockquote>
	 * 
	 */
	public static RuleFormat ruleStringFormat(String ruleString) {
		Pattern p;
		Matcher m;
		byte i = 0;

		if(ruleString != null) {
			do {
				p = Pattern.compile(RuleFormat.values()[i].ruleFormatString);
				m = p.matcher(ruleString.toUpperCase().trim());
			} while (++i < (RuleFormat.values().length ) && !m.matches());
			
			if(ruleString.indexOf("9") != -1) 
				i = 4;
		} else {
			i = 4;
		}
		
		return RuleFormat.values()[i-1];
	}

	/**
	 * <blockquote>
	 * <b><i>validRuleString</i></b>
	 * <pre>{@code}public static boolean validRuleString({@link String} ruleString)</pre>
	 * <p>Checks if given rule is valid or not.</p>
	 * @param ruleString - the String object of the rule you want to validate
	 * @return either false or true, according to if its valid or not. 
	 * </blockquote>
	 * 
	 */
	
	public static boolean validRuleString(String ruleString) {
		return (ruleStringFormat(ruleString) != RuleFormat.INVALID);		
	}
	
	/**
	 * <blockquote>
	 * <b><i>setRules</i></b>
	 * <pre>{@code}public void setRules({@link String} ruleString)</pre>
	 * <p>Sets chosen rule String as the Rule that the Game is running accordingly to.</p>
	 * @param ruleString - The String object of the rule you want to set. 
	 * </blockquote>
	 * 
	 */
	
	public void setRules(String ruleString) {
		RuleFormat rf = ruleStringFormat(ruleString);
		String regExp = rf.ruleFormatString();
		String survive;
		String birth;
		
		switch (rf) {
			case SURVIVE_THEN_BIRTH:
			case BIRTH_THEN_SURVIVE:
				survive = getRuleDataFromStr(ruleString, regExp.split("/")[0], 1);
				birth   = getRuleDataFromStr(ruleString, regExp.split("/")[1], 1);
				break;
			case LEGACY:
				survive = getRuleDataFromStr(ruleString, regExp, 1);
				birth   = getRuleDataFromStr(ruleString, regExp, 2);
				break;
			default:
				//If invalid rule String the rules are set to the standard rules of conway's game of life.
				survive = "23";
				birth = "3";
				break; 
		}
		
		setRuleTabVal(rulesSurvive,survive);
		setRuleTabVal(rulesBirth, birth);
	}
	
	/**
	 * <blockquote>
	 * <b><i>setRuleTabVal</i></b>
	 * <pre>{@code}public void setRuleTabVal(boolean[] ruleArray, {@link String} ruleValues)</pre>
	 * <p>Takes the Rule values, either Survive  or Birth values, and sets it to its correspondent value in the boolean array.  </p>
	 * 
	 * @param ruleArray - boolean array
	 * @param ruleValues - the values of a given Rule
	 * 
	 * </blockquote>
	 */
	
	public void setRuleTabVal(boolean[] ruleArray, String ruleValues) {
		int element,i;
		
		for(i = 0; i < ruleArray.length; i++) {
			ruleArray[i] = false;
		}

		for(i = 0; i < ruleValues.length(); i++ ) {
			element = ruleValues.charAt(i)-48;
			
			if(element > -1 && element < 9) {
				ruleArray[element] = true;
			}
		}
			
	}
	
	/**
	 * <blockquote>
	 * <b><i>getRuleDataFromStr</i></b>
	 * <pre>{@code}public {@link String} getRuleDataFromStr({@link String} ruleString, String regExp, int group)</pre>
	 * <p>Gets the rule data from chosen or active String. Trims the string according to 
	 * Regual expression's for the String.</p>
	 * 
	 * @param ruleString - the active string of the rule. 
	 * @param regExp - the regular expression for rule string.
	 * @param group - the data group of the given rule
	 * @return Group - returns the  group of given rule data. 
	 * 
	 * </blockquote>
	 */
	
	public String getRuleDataFromStr(String ruleString, String regExp, int group) {
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(ruleString.toUpperCase().trim());
		
		m.find();
		String Group=m.group(group);
		return Group;
	}
	
	/**
	 * <blockquote>
	 * <b><i>getRuleString</i></b>
	 * <pre>{@code}public {@link String} getRuleString()</pre>
	 * <p>Returns the RuleString according to its format where S is for survive, and B is for birth.</p>
	 * 
	 * @return RuleString - returns the RuleString.  
	 * 
	 * </blockquote>
	 */
	
	public String getRuleString() {
		String RuleString=("S" + getRuleTabVal(rulesSurvive) + "/" +
				"B" + getRuleTabVal(rulesBirth));
		
		return RuleString;
				
		
	}
	
	/**
	 * <blockquote>
	 * <b><i>getRuleTabVal</i></b>
	 * <pre>{@code}public {@link String} getRuleTabVal(boolean[] ruleArray)</pre>
	 * <p>Gets the rule Table value, and returns the values of the String in a string format.</p>
	 * 
	 * @param ruleArray - takes ruleArray as parameter where it returns the values of the rule-
	 * @return Values - returns the values of the rules in the rulearray, in a String format.  
	 * 
	 * </blockquote>
	 */
	
	public String getRuleTabVal(boolean[] ruleArray) {
		StringBuffer values = new StringBuffer();
		for(byte i = 0; i < ruleArray.length; i++) {
			if(ruleArray[i]) {
				values.append(i);
			}
		}
		String Values= values.toString();
		return Values;
	}
}
