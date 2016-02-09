package model.junit;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class FilehandlingTest {

//	@Test
	public void testRegexComment() {
		String regex = "^\\s*#N\\s+(.*)";
		String testString = "#N  Gosper glider gun";
		
		Pattern p;
		Matcher m;

		p = Pattern.compile(regex);
		m = p.matcher(testString);
		
		System.out.println(m.matches() + "\n" + m.group(1));
	}
	
//	@Test
	public void testRegexCoords() {
		String regex = "^\\s*#R\\s+(\\-?\\d+)\\s+(\\-?\\d+)\\s*$";
		String testString = "#R 10 0";
		
		
		Pattern p;
		Matcher m;

		p = Pattern.compile(regex);
		m = p.matcher(testString);
		System.out.println(m.matches());
		System.out.println(m.matches() + "\n" + m.group(2));
	}

//	@Test
	public void testRegexRules() {
		String regex = "^\\s*#r\\s+((([Bb]\\d*)/([Ss]\\d*))|(([Ss]\\d*)/([Bb]\\d*))|((\\d*)/(\\d*)))\\s*$";
		
		String testString = "#r 23/3";
		
		Pattern p;
		Matcher m;

		p = Pattern.compile(regex);
		m = p.matcher(testString);
		System.out.println(m.matches());
		System.out.println(m.group(1));
	}
	
//	@Test
	public void testRegexSizeWithRules() {
		// group(1) = x, group(2) = y, group(4) = rulestring
		String regex = "^\\s*[Xx]\\s*=\\s*(\\d+)\\s*,\\s*[Yy]\\s*=\\s*(\\d+)(,\\s*[Rr][Uu][Ll][Ee]\\s*=\\s*(([Bb](\\d+)/[Ss](\\d+))|([Ss](\\d+)/[Bb](\\d+))|((\\d+)/(\\d+))))?\\s*$";
		String testString =  "x = 10, y = 3";
		
		Pattern p;
		Matcher m;

		p = Pattern.compile(regex);
		m = p.matcher(testString);
		System.out.println(m.matches());
		System.out.println(m.group(1));
	}
	
//	@Test
	public void testRegexBirthSurvive() {
		String regex = "\\s*(((\\d*([Oo]|[A-Za-z]))|[$])\\s*)+(!.*)*";
		String testString =  "2 o4b$";
		
		Pattern p;
		Matcher m;

		p = Pattern.compile(regex);
		m = p.matcher(testString);

		assertEquals(true, m.matches());
	}
	
//	@Test
	public void testRegexBirthSurvive2() {
		String regex = "\\s*(((\\d*([Oo]|[Bb]))|[$])\\s*)";
		String testString =  "14b2o14o$";
		
		Pattern p;
		Matcher m;

		p = Pattern.compile(regex);
		m = p.matcher(testString);
		
		while(m.find()) {
			System.out.println(m.group(1) + " " + m.start());
		}
	}
	
	@Test
	public void textRegexDecode() {
		String regex = "(\\d*)([Bb]|[Oo])";
		String testString =  "2o4bob";
		int length = 0;
		Pattern p;
		Matcher m;

		p = Pattern.compile(regex);
		m = p.matcher(testString);
		
		while(m.find()) {
			System.out.println(m.group(0) + " = " + m.group(1) + " " + m.group(2));
		}
		
//		m.matches();
		
//		length = Integer.parseInt(m.group(1));
//		int i = 0;
//		do {
//			System.out.println(length + " " + m.group(2));
//		} while(++i < length);
		

	}
}
