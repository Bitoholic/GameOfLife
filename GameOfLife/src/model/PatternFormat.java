package model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * This class parses, controls, and sets the different data that exists in a RLE file.
 *
 *@author Ali Arfan
 *@author Kent Erlend Bratteng Knudsen
 *@author Stian TornHolm Grimsgaard
 * 
 */

public class PatternFormat {
	public static enum RLEFormat {
		PATTERN_NAME			("^\\s*#N\\s+(.*)"),
		PATTERN_CREATOR			("^\\s*#O\\s+(.*)"),
		PATTERN_COMMENT			("^\\s*#[Cc]\\s+(.*)"), 
		COORDS_LIFE32_FORMAT	("^\\s*#P\\s+(\\d+)\\s+(\\d+)\\s*$"),
		COORDS_XLIFE_FORMAT		("^\\s*#R\\s+(\\-?\\d+)\\s+(\\-?\\d+)\\s*$"),  
		PATTERN_XLIFE_RULE		("^\\s*#r\\s+((([Bb]\\d*)/([Ss]\\d*))|(([Ss]\\d*)/([Bb]\\d*))|((\\d*)/(\\d*)))\\s*$"), 
		PATTERN_SIZE_RULE		("^\\s*[Xx]\\s*=\\s*(\\d+)\\s*,\\s*[Yy]\\s*=\\s*(\\d+)"  
								+ "(,\\s*[Rr][Uu][Ll][Ee]\\s*=\\s*("
								+ "([Bb](\\d+)/[Ss](\\d+))|"
								+ "([Ss](\\d+)/[Bb](\\d+))|"
								+ "((\\d+)/(\\d+))))?\\s*$"),
		PATTERN_DATA_TEST		("\\s*(((\\d*([Oo]|[A-Za-z]))|[$])\\s*)+(!.*)*"),
		PATTERN_DATA_STRING		("\\s*((\\d*([Oo]|[A-Za-z]|[$]))\\s*)"),
		PATTERN_DATA_PARSE		("(\\d*)([A-Za-z]|[Oo]|[$])");
		
		private final String dataString;
		
		private RLEFormat(String headerString) {
			this.dataString = headerString;
		}
	}
	
	private BoardDynamic rleCellStateTable;
	
	private StringBuffer patternName	= new StringBuffer();
	private StringBuffer patternCreator	= new StringBuffer();
	private StringBuffer patternComment	= new StringBuffer();
	private int patternCornerY = 0;
	private int patternCornerX = 0;
	private int patternWidth;
	private int patternHeight;
	private boolean patternXLife;
	private String patternRule;
	
	private int patternPosX = 0;
	private int patternPosY = 0;
	private int bitPosition = 0;
	
	
	
	/**
	 *
	 * <blockquote>
	 * <b><i>PatternFormat</i></b>
	 * <pre>{@code public PatternFormat({@link ArrayList} rleData)}</pre>
	 * <p>  An object of patternFormat is created using the read data from the RLE file. The data which is parsed into several parts, 
	 * where the name of the pattern, creator, comment, Life32/Xlife format, array size and 
	 * the pattern is parsed out, and set according to its given purpose. </p>
	 * @param rleData - the read String list of data read from the RLE file. 
	 * @throws PatternFormatException - Throws an exception when the RLE data is incorrect.
	 * </blockquote>
	 * 
	 */
	
	public PatternFormat(List<String> rleData) throws PatternFormatException {
		boolean rleHeader = true;
		boolean stopParser = false;
		boolean noMatches = true;
		
		// We loop through the RLEFormat Enum to see if any data matches.
		for(int j = 0; j < RLEFormat.values().length; j++) {
			
			RLEFormat format = RLEFormat.values()[j];
			Pattern p = Pattern.compile(format.dataString);
			
			// We take one enum value at a time and checks it against the RLE Data.
			for(int i = 0; i < rleData.size(); i++) {
				
				Matcher m = p.matcher(rleData.get(i));
				
				if(m.matches()) {
					if(rleHeader && stopParser == false) {
						switch(format) {
							case PATTERN_NAME:
									noMatches = false;
									setPatternName(m.group(1));
								break;
							case PATTERN_CREATOR:
								noMatches = false;
								setPatternCreator(m.group(1));
								break;
							case PATTERN_COMMENT:
								noMatches = false;
								setPatternComment(m.group(1));
								break;
							case COORDS_LIFE32_FORMAT:
								noMatches = false;
								setPatternFormat(Integer.parseInt(m.group(1)),
										Integer.parseInt(m.group(2)), false);
								break;
							case COORDS_XLIFE_FORMAT:
								noMatches = false;
								setPatternFormat(Integer.parseInt(m.group(1)),
										Integer.parseInt(m.group(2)), true);
								break;
							case PATTERN_XLIFE_RULE:
								noMatches = false;
								setPatternRule(m.group(1));
								break;
							case PATTERN_SIZE_RULE:
								
								noMatches = false;
								setPatternSize(Integer.parseInt(m.group(1)),
										Integer.parseInt(m.group(2)));

								setPatternRule(m.group(4));	
							default:
								break;
						}
					}
					
					if(format == RLEFormat.PATTERN_DATA_TEST) {
						/*
						 * Since header data should be found in the header of a file,
						 * we tell the program it shouldn't scan for header data when
						 * pattern data is found.
						 */
						
						noMatches = false;
						rleHeader = false;
						
						if(rleCellStateTable != null && stopParser != true) {
							int endOfData = -1;
							
							if(m.group(5) != null){
								/*
								 *  The "!" character at the end of RLE data,
								 *  tells us to stop looking for more RLE data.
								 *  
								 *  Anything after this is not for decoding.
								 */
								endOfData  = rleData.get(i).indexOf("!");
								stopParser = true;
							}
							
							Pattern pData = Pattern.compile(RLEFormat.PATTERN_DATA_STRING.dataString);
							Matcher mData = pData.matcher(rleData.get(i));

							while(mData.find()) {
								if(mData.start() < endOfData || endOfData == -1)
									decode(mData.group(1));
							}
	
						} else if(rleCellStateTable == null) {
							throw new PatternFormatException(
									"RLE pattern dimension has not been defined in header\n" +
									"or header is invalid.");
						}
					}
				}
			}
		}
		
		if(noMatches) {
			throw new PatternFormatException("Not a valid RLE format.");
		}
	}
	

	/**
	 * 
	 * <blockquote>
	 * <b><i>decode</i></b>
	 * <pre>{@code}public void decode({@link String} data)</pre>
	 * <p> decodes the cell state, where the cell of the state is either dead or alive.</p>
	  * @param data -  A string containing all the state of the cells in the array.
	 * </blockquote>
	 * 
	 */
	
	public void decode(String data) {
		boolean cellState = false;
		int runLength = 0;
		
		Pattern p;
		Matcher m;

		p = Pattern.compile(RLEFormat.PATTERN_DATA_PARSE.dataString);
		m = p.matcher(data);
		
		if(m.matches()) {
			if(m.group(1).length() > 0) {
				runLength = Integer.parseInt(m.group(1));
			} else {
				runLength = 1;
			}

			switch(m.group(2).toLowerCase()) {
				case "o":
					cellState = true;
				case "b":
					for(int i = 0; i < runLength; i++){
						rleCellStateTable.setCellState(patternPosX*64+bitPosition, patternPosY, cellState);
						
						bitPosition++;
						
						if(bitPosition == 64) {
							bitPosition = 0;
							patternPosX++;
						}
						
						if((patternPosX * 64 + bitPosition) > patternWidth) {
							bitPosition = 0;
							patternPosX = 0;
							patternPosY++;
						}
					}
					break;
				case "$":
					bitPosition = 0;
					patternPosX = 0;
					patternPosY += runLength;
					break;
			}
		}
	}
	
	
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>setPatternCreator</i></b>
	 * <pre>{@code}public void decode({@link String} patternCreator)</pre>
	 * <p> Sets the author of the pattern that is loaded.</p>
	 * @param patternCreator - a string containing the name of the author. 
	 * </blockquote>
	 * 
	 */
	public void setPatternCreator(String patternCreator) {
		this.patternCreator.append(patternCreator + "\n");
	}
	
	
	
	/**
	 *
	 * <blockquote>
	 * <b><i>setPatternName</i></b>
	 * <pre>{@code}public void setPatternName({@link String} patternName)</pre>
	 * <p> Sets the  name of the pattern that is loaded.</p>
	  * @param patternName - a string containing the name of the pattern.
	 * </blockquote>
	 * 
	 */
	
	public void setPatternName(String patternName) {
		this.patternName.append(patternName + "\n");
	}
	
	
	
	/**
	 *
	 * <blockquote>
	 * <b><i>setPatternComment</i></b>
	 * <pre>{@code}public void setPatternComment({@link String} patternComment)</pre>
	 * <p> Sets the comment that is specified in the RLE file.</p>
	  * @param patternComment - a string containing the comments in the RLE file.
	 * </blockquote>
	 * 
	 */
	
	public void setPatternComment(String patternComment) {
		this.patternComment.append(patternComment + "\n");
	}

	
	
	/**
	 *
	 * <blockquote>
	 * <b><i>setPatternSize</i></b>
	 * <pre>{@code}public void setPatternSize({@link Integer} width, {@link Integer} height)</pre>
	 * <p>  An array is created using the  dimension of the pattern specified in the loaded file. </p>
	 * @param width - the width of the pattern, specified as X={@link Integer}  in the RLE file .
	 * @param height the height of the pattern, specified as Y={@link Integer}in the RLE file
	 * @throws PatternFormatException - throws an exception if the pattern dimensions are negative.
	 * </blockquote> 
	 * 
	 */
	
	public void setPatternSize(int width, int height) throws PatternFormatException {
		if(width < 0 || height < 0) {
			throw new PatternFormatException("Pattern dimension cannot be negative."); 
		}
		
		patternWidth  = width;
		patternHeight = height;
		
		rleCellStateTable = new BoardDynamic(patternHeight,patternWidth);
	}
	
	
	
	/**
	 *
	 * <blockquote>
	 * <b><i>setPatternFormat</i></b>
	 * <pre>{@code}public void setPatternFormat({@link Integer} topLeftX, {@link Integer} topLeftY, {@link Boolean} XLifeFormat)</pre>
	 * <p>Sets the format of the pattern which is either Xlife or Life32  </p>
	 * @param topLeftX - the vertical coordinate of the pattern starting from the top left corner. 
	 * @param topLeftY - the horizontal coordinate of the pattern starting from the top left corner.
	 * @param XLifeFormat -  The value is true if  the loaded pattern is the XLife format, else its false.
	 * @throws PatternFormatException  - Throws an exception if the x or y coordinate is not matching the format. 
	 * </blockquote> 
	 * 
	 */
	
	public void setPatternFormat(int topLeftX, int topLeftY, boolean XLifeFormat) throws PatternFormatException {
		if(XLifeFormat) {
			if(Math.abs(topLeftX) > (patternWidth / 2) || Math.abs(topLeftY) > (patternHeight / 2)) {
				throw new PatternFormatException("Top left corner cannot exceed defined pattern size.");
			}
		} else {
			if(topLeftX < 0 || topLeftY < 0) {
				throw new PatternFormatException("Top left corner cannot be negative for the Life32 format");
			} else if(topLeftX > patternWidth || topLeftY > patternHeight) {
				throw new PatternFormatException("Top left corner cannot exceed defined pattern size.");
			}
		}
		
		patternXLife = XLifeFormat;
		patternCornerX = topLeftX;
		patternCornerY = topLeftY;
	}
	
	
	
	/**
	 *
	 * <blockquote>
	 * <b><i>setPatternRule</i></b>
	 * <pre>{@code}public void setPatternRule({@link String} ruleString)</pre>
	 * <p>Sets the rule of the pattern, which decides how many cells survive or dies.  </p>
	 * @param ruleString - The string containing the rule	
	 * @throws PatternFormatException - Throws an error if the rule String is invalid.
	 * </blockquote> 
	 */
	
	public void setPatternRule(String ruleString) throws PatternFormatException {
		if(GameRules.validRuleString(ruleString)) {
			patternRule = ruleString;
		} else {
			throw new PatternFormatException("Invalid rulestring format.");
		}
	}
	
	/**
	 *
	 * <blockquote>
	 * <b><i>getPatternWidth</i></b>
	 * <pre>{@code}public int getPatternWidth()</pre>
	 * <p>Returns the width of the pattern, its specified as x=Integer in the RLE file.</p>
	 * @return patternWidth returns the width.	
	 * </blockquote> 
	 */
	
	public int getPatternWidth() {
		return patternWidth;
	}
	
	/**
	 *
	 * <blockquote>
	 * <b><i>getPatternHeight</i></b>
	 * <pre>{@code}public int getPatternHeight()</pre>
	 * <p>Returns the height of the pattern, its specified as y=Integer in the RLE file.</p>
	 * @return patternWidth returns the height.
	 * </blockquote> 
	 */
	public int getPatternHeight() {
		return patternHeight;
	}

	/**
	 *
	 * <blockquote>
	 * <b><i>getPattern</i></b>
	 * <pre>{@code}public {@link BoardDynamic} getPattern()</pre>
	 * <p>Returns the array containing the cellstates of the pattern. either dead or alive. </p>
	 * @return patternTable - the array containing the cellstates for retrieved from the RLE file.  
	 * </blockquote> 
	 */
	
	public BoardDynamic getPattern() {
		return rleCellStateTable;
	}
	
	/**
	 *
	 * <blockquote>
	 * <b><i>getPatternCornerY</i></b>
	 * <pre>{@code}public int getPatternCornerY()</pre>
	 * @return patternCornerY - top left corner relative to mousepointer
	 * </blockquote> 
	 */
	
	public int getPatternCornerY() {
		return patternCornerY;
	}
	
	/**
	 *
	 * <blockquote>
	 * <b><i>getPatternCornerX</i></b>
	 * <pre>{@code}public int getPatternCornerX()</pre>
	 * @return patternCornerX - top left corner relative to mouse pointer.
	 * </blockquote> 
	 */
	
	public int getPatternCornerX() {
		return patternCornerX;
	}
	
	/**
	 *
	 * <blockquote>
	 * <b><i>getPatternCornerXLife</i></b>
	 * <pre>{@code}public int getPatternCornerXLife()</pre>
	 * @return patternXLife - XLifeFormat or Life32
	 * </blockquote> 
	 */
	
	public boolean getPatternXLife() {
		return patternXLife;
	}
	
	/**
	 *
	 * <blockquote>
	 * <b><i>getPatternRule</i></b>
	 * <pre>{@code}public String getPatternRule()</pre>
	 * @return patternRule - returns the pattern rule String.
	 * </blockquote> 
	 */
	
	public String getPatternRule() {
		return patternRule;
	}
}