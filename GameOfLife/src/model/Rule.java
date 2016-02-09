package model;

import java.io.Serializable;

/**
 * This class is used to create a new rule, an new object is created when a new rule is defined or created.
 * 
 * @author Ali Arfan
 * @author Kent Erlend Bratteng Knudsen
 * @author Stian Tornholm Grimsgaard
 */
public class Rule implements Serializable, Comparable<Rule> {
	static final long serialVersionUID = 30032016L;

	private int index = 0;
	private String ruleString;
	private String ruleName;
	private String ruleDescription;
	
	
	/**
	 * <blockquote>
	 * <b><i>getRuleString</i></b>
	 * <pre>{@code}public String getRuleString()</pre>
	 * <p>Returns the rule string.</p>
	 * 
	 * @return ruleString -  the ruleString used in the active rule.
	 * </blockquote>
	 */
	public String getRuleString() {
		return ruleString.toString();
	}
	
	
	/**
	 * <blockquote>
	 * <b><i>setRuleString</i></b>
	 * <pre>{@code}public String setRuleString()</pre>
	 * <p>Sets the value to a rule string.</p>
	 * @param ruleString - String containing the rule.
	 * </blockquote>
	 */
	protected void setRuleString(String ruleString){
		this.ruleString = ruleString;
	}
	
	
	/**
	 * <blockquote>
	 * <b><i>getRuleName</i></b>
	 * <pre>{@code}public {@link String} getRuleName()</pre>
	 * <p>Returns name of the rule object.</p>
	 * 
	 * @return ruleName - name of the rule. 
	 * </blockquote>
	 */
	public String getRuleName() {
		return ruleName.toString();
	}
	
	/**
	 * <blockquote>
	 * <b><i>setRuleName</i></b>
	 * <pre>{@code}public {@link String} setRuleName()</pre>
	 * <p>Sets a name to the rule object.</p>
	 * 
	 * @param ruleName
	 * </blockquote>
	 */
	protected void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	
	
	/**
	 * <blockquote>
	 * <b><i>getRuleIndex</i></b>
	 * <pre>{@code}public int getRuleIndex()</pre>
	 * <p>Returns the rule index.</p>
	 * 
	 * @return index - position of the rule in the array. 
	 * </blockquote>
	 */
	public int getRuleIndex() {
		return index;
	}
	
	
	/**
	 * <blockquote>
	 * <b><i>setRuleIndex</i></b>
	 * <pre>{@code}public int setRuleIndex()</pre>
	 * <p>Sets the rule index.</p>
	 * 
	 * @param index - integer value with the index of the rule object. 
	 * </blockquote>
	 */
	protected void setRuleIndex(int index) {
		this.index = index;
	}
	
	
	/**
	 * <blockquote>
	 * <b><i>getRuleDescription</i></b>
	 * <pre>{@code}public {@link String} getRuleDescription()</pre>
	 * <p>Returns the description of the rule.</p>
	 * 
	 * @return ruleDescription - The description of the rule.
	 * </blockquote>
	 */
	public String getRuleDescription() {
		return ruleDescription.toString();
	}
	
	
	/**
	 * <blockquote>
	 * <b><i>setRuleDescription</i></b>
	 * <pre>{@code}public {@link String} setRuleDescription()</pre>
	 * <p>Sets the description of the rule.</p>
	 * 
	 * @param ruleDescription - The description of the rule.
	 * </blockquote>
	 */
	protected void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}
	
	/**
	 * <blockquote>
	 * <b><i>compareTo</i></b>
	 * <pre>{@code}public int compareTo({@link Rule} rule)</pre>
	 * <p>Compares  a rule to existing rules, and returns the index position.</p>
	 * 
	 * @param ruleCompare - rule that is compared to the rules in the index.
	 * </blockquote>
	 */
	@Override
	public int compareTo(Rule ruleCompare) {
		int index = this.index - ruleCompare.getRuleIndex();
		return index;
	}
	
	/**
	 * <blockquote>
	 * <b><i>toString</i></b>
	 * <pre>{@code}public {@link String} toString()</pre>
	 * <p>converts all the  rule information to String, and returns it</p>
	 * 
	 * @return string - information about the active rule.
	 * </blockquote>
	 */
	@Override
	public String toString() {
		return ("[index=" + index + ", ruleName=" + ruleName + 
				", ruleString=" + ruleString + ", ruleDescription=" + ruleDescription + "]");
	}
}
