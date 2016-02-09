package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.openmbean.OpenMBeanOperationInfoSupport;

import org.junit.Test;

import model.Rule;
import model.GameRules;
import model.GameRules.RuleFormat;



public class GameRulesTest {

	public GameRules gr = GameRules.getInstance();
	
	
//	@Test
	public void testSomeRegEx() {
		Pattern p;
		Matcher m;
		
		String testString = "S23/B3";

		p = Pattern.compile("S(|[0-8]+)/B(|[0-8]+)");
		m = p.matcher(testString);

		System.out.println(m.matches() + " " );
	}
	
	
//	@Test
	public void testSetRuleString() {		
		String ruleString = "S012345678/B3";
		
		gr.setRules(ruleString);
		
		System.out.println(gr.getRuleString());
	}
	
	
//	@Test
	public void testMakeFile() throws IOException {
		Path p = Paths.get("rules.ser");
		InputStream reader = Files.newInputStream(p);
		ObjectInputStream ois = new ObjectInputStream(reader);
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadRulesFile() throws IOException, ClassNotFoundException {
		Path path = Paths.get("rules.ser");
		FileOutputStream fos = new FileOutputStream(path.toFile());
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		Rule ruleOut;
		List<Rule> r = new ArrayList<>();
		
		ruleOut = new Rule();
		ruleOut.index = 1;
		ruleOut.ruleString = "S1/B1";
		ruleOut.ruleName = "Gnarl";
		ruleOut.ruleDescription = "A simple exploding rule that forms complex patterns from even a single live cell.";
		r.add(ruleOut);
//		oos.writeObject(ruleOut);
		
		ruleOut = new Rule();
		ruleOut.index = 2;
		ruleOut.ruleString = "S1357/B1357";
		ruleOut.ruleName = "Replicator";
		ruleOut.ruleDescription = "A rule in which every pattern is a replicator.";
		r.add(ruleOut);
//		oos.writeObject(ruleOut);
		
		oos.writeObject(r);
		
		oos.close();
		fos.close();
		
		FileInputStream fis = new FileInputStream(path.toFile());
		ObjectInputStream ois = new ObjectInputStream(fis);
		Rule ruleInn = new Rule();
		
		r.clear();
		
//		ruleInn = (Rule) ois.readObject();
		
		r = (List<Rule>) ois.readObject();
		System.out.println(r.size());
		
		System.out.println(ruleInn.ruleString);
		
		ruleInn = (Rule) ois.readObject();
		System.out.println(ruleInn.ruleString);
		
		ois.close();
		fis.close();
		
//		System.out.println("Rule index:       " + ruleInn.index);
//		System.out.println("Rule string:      " + ruleInn.ruleString);
//		System.out.println("Rule name:        " + ruleInn.ruleName);
//		System.out.println("Rule description: " + ruleInn.ruleDescription);
		
//		gr.loadRulesFile();
	}
}
