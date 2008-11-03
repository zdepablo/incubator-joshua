/* This file is part of the Joshua Machine Translation System.
 * 
 * Joshua is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 */
package joshua.decoder.ff.tm;

import java.util.Map;

import joshua.decoder.ff.tm.TMGrammar;
import joshua.decoder.Symbol;
import joshua.util.sentence.Vocabulary;


/**
 * 
 * @author Zhifei Li, <zhifei.work@gmail.com>
 * @version $LastChangedDate: 2008-08-03 04:12:57 -0400 (Sun, 03 Aug 2008) $
 */
public class Rule {
	/* The string format of Rule is:
	 *     [Phrase] ||| french ||| english ||| feature scores
	 */
	public  int     rule_id;
	public  int     lhs;         // tag of this rule, state to upper layer
	public  int[]   french;      // only need to maintain at rulebine
	public  int[]   english;
	
	/* a feature function will be applied  for this rule 
	 * only if the owner of the rule matches the owner of the feature function
	 * */
	public  int     owner;
	
	public  float[] feat_scores; // the feature scores for this rule
	public	float lattice_cost;
	public  int     arity;// = 0;   // TODO: disk-grammar does not have this information, so, arity-penalty feature is not supported in disk-grammar
	
	/* this remember all the stateless cost: sum of cost of all
	 * stateless models (e..g, phrase model, word-penalty,
	 * phrase-penalty). The LM model cost is not included here.
	 * this will be set by Grmmar.estimate_rule
	 */
	protected float statelesscost = 0; //TODO: this is set in estimate_rule(); we should use an abstract class to enforce this behavior

		
	
//	TODO Ideally, we shouldn't have to have dummy rule IDs and dummy owners. How can this need be eliminated?
	private static final int DUMMY_RULE_ID = 1;
	private static final int DUMMY_OWNER = 1;
	
	public Rule(){
		//do nothing
	}
	

	/**
	 * For use in constructing out of vocabulary rules.
	 * 
	 * @param rule_id
	 * @param lhs Left-hand side of the rule.
	 * @param fr_in Source language right-hand side of the rule.
	 * @param owner
	 */
	public Rule(int rule_id, int lhs, int fr_in, int owner, int num_feats) {
		this.rule_id    = rule_id;
		this.lhs        = lhs;
		this.owner      = owner;
		this.arity      = 0;
		this.french     = new int[1];
	   	this.french[0]  = fr_in;
	   	this.english    = new int[1];
	   	this.english[0] = fr_in;
	   	feat_scores     = new float[num_feats];
		lattice_cost	= 0;
	}
	
	
	/**
	 * For use in reading rules from disk.
	 * 
	 * @param rule_id
	 * @param lhs Left-hand side of the rule.
	 * @param fr_in Source language right-hand side of the rule.
	 * @param eng_in Target language right-hand side of the rule.
	 * @param owner
	 * @param feat_scores_in Feature value scores for the rule.
	 * @param arity_in Number of nonterminals in the source language right-hand side.
	 */
	public Rule(int rule_id, int lhs, int[] fr_in, int[] eng_in, int owner, float[] feat_scores_in, int arity_in) {
		this.rule_id     = rule_id;
		this.lhs         = lhs;
		this.french      = fr_in;
		this.english     = eng_in;
		this.owner       = owner;
		this.feat_scores = feat_scores_in;
		this.arity       = arity_in;
		this.lattice_cost= 0;
	}
	
	// just used by cloneAndAddLatticeCost:
	private Rule(int rule_id, int lhs, int[] fr_in, int[] eng_in, int owner, float[] feat_scores_in, int arity_in, float src_cost) {
		this.rule_id     = rule_id;
		this.lhs         = lhs;
		this.french      = fr_in;
		this.english     = eng_in;
		this.owner       = owner;
		this.feat_scores = feat_scores_in;
		this.arity       = arity_in;
		this.lattice_cost = src_cost;
	}
	
	
	/**
	 * Constructs a new rule using the provided parameters.
	 * The owner and rule id for this rule are undefined.
	 * 
	 * @param lhs Left-hand side of the rule.
	 * @param source_rhs Source language right-hand side of the rule.
	 * @param target_rhs Target language right-hand side of the rule.
	 * @param feature_scores Feature value scores for the rule.
	 * @param arity Number of nonterminals in the source language right-hand side.
	 */
	public Rule(int lhs, int[] source_rhs, int[] target_rhs, float[] feature_scores, int arity) {
		this(DUMMY_RULE_ID, lhs, source_rhs, target_rhs, DUMMY_OWNER, feature_scores, arity);
	}
	
	
	public float getStatelessCost(){
		return statelesscost;
	}
	
	
//	 create a copy of the rule and set the lattice cost field
	public Rule cloneAndAddLatticeCost(float cost) {
		Rule r = new Rule(rule_id, lhs, french, english, owner, feat_scores, arity, cost);
		return r;
	}
	
	/* ~~~~~ Attributes (only used in DiskHyperGraph) */
	public final boolean isOutOfVocabularyRule() {
		return (this.rule_id == TMGrammar.OOV_RULE_ID);
	}
	
	
	public final int getRuleID() {
		return this.rule_id;
	}
	
	
	/* ~~~~~ Serialization methods */
	// Caching this method significantly improves performance
	// We mark it transient because it is, though cf java.io.Serializable
	private transient String cachedToString = null;
	
	public String toString(Map<Integer,String> ntVocab, Vocabulary sourceVocab, Vocabulary targetVocab) {
		if (null == this.cachedToString) {
			StringBuffer sb = new StringBuffer("[");
			sb.append(ntVocab.get(this.lhs));
			sb.append("] ||| ");
			sb.append(sourceVocab.getWords(this.french,true));
			sb.append(" ||| ");
			sb.append(targetVocab.getWords(this.english,false));
			//sb.append(java.util.Arrays.toString(this.english));
			sb.append(" |||");
			for (int i = 0; i < this.feat_scores.length; i++) {
				sb.append(String.format(" %.4f", this.feat_scores[i]));
			}
			this.cachedToString = sb.toString();
		}
		return this.cachedToString;
	}
	
	public String toString(Symbol p_symbol) {
		if (null == this.cachedToString) {
			StringBuffer sb = new StringBuffer("[");
			sb.append(p_symbol.getWord(this.lhs));
			sb.append("] ||| ");
			sb.append(p_symbol.getWords(this.french));
			sb.append(" ||| ");
			sb.append(p_symbol.getWords(this.english));
			sb.append(" |||");
			for (int i = 0; i < this.feat_scores.length; i++) {
				sb.append(String.format(" %.4f", this.feat_scores[i]));
			}
			this.cachedToString = sb.toString();
		}
		return this.cachedToString;
	}
	
    public String toStringWithoutFeatScores(Symbol p_symbol){
            StringBuffer res = new StringBuffer();
            res.append("["); res.append(p_symbol.getWord(lhs)); res.append("] ||| ");
            res.append(p_symbol.getWords(french)); res.append(" ||| ");
            res.append(p_symbol.getWords(english));
            return res.toString();
    }
}
