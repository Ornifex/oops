/**
 * This program (working title: MAS Prover) is an automated tableaux prover
 * for epistemic logic (S5n).
 * Copyright (C) 2007  Elske van der Vaart and Gert van Valkenhoef

 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package nl.rug.ai.mas.oops.tableau;

import nl.rug.ai.mas.oops.formula.*;
import nl.rug.ai.mas.oops.parser.Context;

import java.util.Vector;

import static nl.rug.ai.mas.oops.render.Constants.DISJ;

public class ModalRuleFactory {
	public static String LOZENGE = "&#9674;";
	public static String SQUARE = "&#9723;";

	private interface RuleClosure {
		Rule buildRule(Context c);
	}

	public enum RuleID {
		Announce1(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildAnnounce1(c);
			}
		}),
		Announce2(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildAnnounce2(c);
			}
		}),
		Sannounce1(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildSannounce1(c);
			}
		}),
		Sannounce2(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildSannounce2(c);
			}
		}),
		PosO1(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildPosO1(c);
			}
		}), PosO2(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildPosO2(c);
			}
		}), PosS1(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildPosS1(c);
			}
		}), PosS2(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildPosS2(c);
			}
		}), BNecO1(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildBNecO1(c);
			}
		}), BNecO2(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildBNecO2(c);
			}
		}), BNecS1(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildBNecS1(c);
			}
		}), BNecS2(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildBNecS2(c);
			}
		}), SNecO1(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildSNecO1(c);
			}
		}), SNecO2(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildSNecO2(c);
			}
		}), SNecO3(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildSNecO3(c);
			}
		}), SNecO4(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildSNecO4(c);
			}
		}), SNecS1(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildSNecS1(c);
			}
		}), SNecS2(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildSNecS2(c);
			}
		}), SNecS3(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildSNecS3(c);
			}
		}), SNecS4(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildSNecS4(c);
			}
		}), EK1(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildEK1(c);
			}
		}), EK2(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildEK2(c);
			}
		}), EK3(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildEK3(c);
			}
		}), EK4(new RuleClosure() {
			public Rule buildRule(Context c) {
				return buildEK4(c);
			}
		});

		private final RuleClosure func;

		RuleID(RuleClosure func) {
			this.func = func;
		}

		public Rule buildRule(Context c) {
			return func.buildRule(c);
		}
	}

	private ModalRuleFactory() {
	}

	public static Vector<Rule> build(Context context) {
		Vector<Rule> rules = new Vector<Rule>(12);

		for (RuleID rule : RuleID.values()) {
			rules.add(rule.buildRule(context));
		}
		return rules;
	}

	public static Vector<Rule> build(Context context, Vector<RuleID> ruleIds) {
		Vector<Rule> rules = new Vector<Rule>();

		for (RuleID rule : ruleIds) {
			rules.add(rule.buildRule(context));
		}
		return rules;
	}

	public static Rule build(Context context, RuleID rule) {
		return rule.buildRule(context);
	}

	public static Rule buildPosO1(Context context) {
		String html = "M<sub>" + LOZENGE + "</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// different agent
		Variable<Agent> j = new Variable<Agent>("j");
		AgentReference jref = new AgentReference(j, context.getAgentCodeMap().code(j));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// new world
		// Variable<World> n = new Variable<World>("n");
		// WorldReference nref = new WorldReference(n);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Formula templateFormula = new MultiDiamond(iref, fref);
		Formula rewriteFormula = fref;
		WorldInstance n = new WorldInstance(rewriteFormula);
		Label templateLabel = new LabelInstance(lref, kref, jref);
		Label rewriteLabel = new LabelInstance(templateLabel, n, iref);
		Constraint c = new NotEqualConstraint(i, j);
		return new CreateRule("PosO1", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula), c);
	}

	public static Rule buildPosO2(Context context) {
		String html = "M<sub>" + SQUARE + "</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// different agent
		Variable<Agent> j = new Variable<Agent>("j");
		AgentReference jref = new AgentReference(j, context.getAgentCodeMap().code(j));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// new world
		// Variable<World> n = new Variable<World>("n");
		// WorldReference nref = new WorldReference(n);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Formula templateFormula = new Negation(new MultiBox(iref, fref));
		Formula rewriteFormula = new Negation(fref);
		WorldInstance n = new WorldInstance(rewriteFormula);
		Label templateLabel = new LabelInstance(lref, kref, jref);
		Label rewriteLabel = new LabelInstance(templateLabel, n, iref);
		Constraint c = new NotEqualConstraint(i, j);
		return new CreateRule("PosO2", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula), c);
	}

	public static Rule buildPosS1(Context context) {
		String html = "M<sub>" + LOZENGE + "*</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// new world
		// Variable<World> n = new Variable<World>("n");
		// WorldReference nref = new WorldReference(n);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Formula templateFormula = new MultiDiamond(iref, fref);
		Formula rewriteFormula = fref;
		WorldInstance n = new WorldInstance(rewriteFormula);
		Label templateLabel = new LabelInstance(lref, kref, iref);
		Label rewriteLabel = new LabelInstance(lref, n, iref);
		return new CreateRule("PosS1", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula));
	}

	public static Rule buildPosS2(Context context) {
		String html = "M<sub>" + SQUARE + "*</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// new world
		// Variable<World> n = new Variable<World>("n");
		// WorldReference nref = new WorldReference(n);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Formula templateFormula = new Negation(new MultiBox(iref, fref));
		Formula rewriteFormula = new Negation(fref);
		WorldInstance n = new WorldInstance(rewriteFormula);
		Label templateLabel = new LabelInstance(lref, kref, iref);
		Label rewriteLabel = new LabelInstance(lref, n, iref);
		return new CreateRule("PosS2", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula));
	}

	public static Rule buildBNecO1(Context context) {
		String html = "K<sub>" + SQUARE + "</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// different agent
		Variable<Agent> j = new Variable<Agent>("j");
		AgentReference jref = new AgentReference(j, context.getAgentCodeMap().code(j));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// new world
		Variable<World> n = new Variable<World>("n");
		WorldReference nref = new WorldReference(n);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Formula templateFormula = new MultiBox(iref, fref);
		Formula rewriteFormula = fref;
		Label templateLabel = new LabelInstance(lref, kref, jref);
		Label rewriteLabel = new LabelInstance(templateLabel, nref, iref);
		Constraint c = new NotEqualConstraint(i, j);
		return new AccessRule("BNecO1", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula), c);
	}

	public static Rule buildBNecO2(Context context) {
		String html = "K<sub>" + LOZENGE + "</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// different agent
		Variable<Agent> j = new Variable<Agent>("j");
		AgentReference jref = new AgentReference(j, context.getAgentCodeMap().code(j));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// new world
		Variable<World> n = new Variable<World>("n");
		WorldReference nref = new WorldReference(n);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Label templateLabel = new LabelInstance(lref, kref, jref);
		Label rewriteLabel = new LabelInstance(templateLabel, nref, iref);
		Formula templateFormula = new Negation(new MultiDiamond(iref, fref));
		Formula rewriteFormula = new Negation(fref);
		Constraint c = new NotEqualConstraint(i, j);
		return new AccessRule("BNecO2", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula), c);
	}

	public static Rule buildBNecS1(Context context) {
		String html = "K<sub>" + SQUARE + "*</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// new world
		Variable<World> n = new Variable<World>("n");
		WorldReference nref = new WorldReference(n);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Label templateLabel = new LabelInstance(lref, kref, iref);
		Label rewriteLabel = new LabelInstance(lref, nref, iref);
		Formula templateFormula = new MultiBox(iref, fref);
		Formula rewriteFormula = fref;
		return new AccessRule("BNecS1", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula));
	}

	public static Rule buildBNecS2(Context context) {
		String html = "K<sub>" + LOZENGE + "*</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// new world
		Variable<World> n = new Variable<World>("n");
		WorldReference nref = new WorldReference(n);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Label templateLabel = new LabelInstance(lref, kref, iref);
		Label rewriteLabel = new LabelInstance(lref, nref, iref);
		Formula templateFormula = new Negation(new MultiDiamond(iref, fref));
		Formula rewriteFormula = new Negation(fref);
		return new AccessRule("BNecS2", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula));
	}

	public static Rule buildSNecO1(Context context) {
		String html = "T<sub>" + SQUARE + "</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// different agent
		Variable<Agent> j = new Variable<Agent>("j");
		AgentReference jref = new AgentReference(j, context.getAgentCodeMap().code(j));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Label templateLabel = new LabelInstance(lref, kref, jref);
		Label rewriteLabel = templateLabel;
		Formula templateFormula = new MultiBox(iref, fref);
		Formula rewriteFormula = fref;
		Constraint c = new NotEqualConstraint(i, j);
		return new AccessRule("SNecO1", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula), c);
	}

	public static Rule buildSNecO2(Context context) {
		String html = "T<sub>" + LOZENGE + "</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// different agent
		Variable<Agent> j = new Variable<Agent>("j");
		AgentReference jref = new AgentReference(j, context.getAgentCodeMap().code(j));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Label templateLabel = new LabelInstance(lref, kref, jref);
		Label rewriteLabel = templateLabel;
		Formula templateFormula = new Negation(new MultiDiamond(iref, fref));
		Formula rewriteFormula = new Negation(fref);
		Constraint c = new NotEqualConstraint(i, j);
		return new AccessRule("SNecO2", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula), c);
	}

	public static Rule buildSNecO3(Context context) {
		String html = "D<sub>" + SQUARE + "</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// different agent
		Variable<Agent> j = new Variable<Agent>("j");
		AgentReference jref = new AgentReference(j, context.getAgentCodeMap().code(j));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Label templateLabel = new LabelInstance(lref, kref, jref);
		Label rewriteLabel = templateLabel;
		Formula templateFormula = new MultiBox(iref, fref);
		Formula rewriteFormula = new MultiDiamond(iref, fref);
		Constraint c = new NotEqualConstraint(i, j);
		return new AccessRule("SNecO3", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula), c);
	}

	public static Rule buildSNecO4(Context context) {
		String html = "D<sub>" + LOZENGE + "</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// different agent
		Variable<Agent> j = new Variable<Agent>("j");
		AgentReference jref = new AgentReference(j, context.getAgentCodeMap().code(j));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Label templateLabel = new LabelInstance(lref, kref, jref);
		Label rewriteLabel = templateLabel;
		Formula templateFormula = new Negation(new MultiDiamond(iref, fref));
		Formula rewriteFormula = new Negation(new MultiBox(iref, fref));
		Constraint c = new NotEqualConstraint(i, j);
		return new AccessRule("SNecO4", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula), c);
	}

	public static Rule buildSNecS1(Context context) {
		String html = "R<sub>" + SQUARE + "*</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Label templateLabel = new LabelInstance(lref, kref, iref);
		Label rewriteLabel = lref;
		Formula templateFormula = new MultiBox(iref, fref);
		Formula rewriteFormula = fref;
		return new AccessRule("SNecS1", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula));
	}

	public static Rule buildSNecS2(Context context) {
		String html = "R<sub>" + LOZENGE + "*</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Label templateLabel = new LabelInstance(lref, kref, iref);
		Label rewriteLabel = lref;
		Formula templateFormula = new Negation(new MultiDiamond(iref, fref));
		Formula rewriteFormula = new Negation(fref);
		return new AccessRule("SNecS2", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula));
	}

	public static Rule buildSNecS3(Context context) {
		String html = "4r<sub>" + SQUARE + "*</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Label templateLabel = new LabelInstance(lref, kref, iref);
		Label rewriteLabel = lref;
		Formula templateFormula = new MultiBox(iref, fref);
		Formula rewriteFormula = new MultiBox(iref, fref);
		return new AccessRule("SNecS3", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula));
	}

	public static Rule buildSNecS4(Context context) {
		String html = "4r<sub>" + LOZENGE + "*</sub>";
		// variables occuring
		// the formula
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f, context.getFormulaCodeMap().code(f));
		// agent for which the modal operator holds
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));
		// current world
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		// superlabel
		Variable<Label> l = new Variable<Label>("L");
		LabelReference lref = new LabelReference(l);

		// formula f. agents i, j. worlds k, n. label l.
		Label templateLabel = new LabelInstance(lref, kref, iref);
		Label rewriteLabel = lref;
		Formula templateFormula = new Negation(new MultiDiamond(iref, fref));
		Formula rewriteFormula = new Negation(new MultiDiamond(iref, fref));
		return new AccessRule("SNecS4", html, new Node(templateLabel, templateFormula), new Node(rewriteLabel, rewriteFormula));
	}

	public static Rule buildEK1(Context context) {
		String html = "E" + "<sub>" + SQUARE + "</sub>";
		// variables
		Variable<Formula> F = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(F, context.getFormulaCodeMap().code(F));

		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));

		// formula
		Formula f = new UniBox(fref);

		// rewrites
		Formula rwt = new MultiBox(iref, fref);
		
		return new AgentLinearRule("EK1", html, f, rwt, context, iref);
	}
	
	public static Rule buildEK2(Context context) {
		String html = "E" + "<sub>" + SQUARE + "*</sub>";
		// variables
		Variable<Formula> F = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(F, context.getFormulaCodeMap().code(F));

		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));

		// formula
		Formula f = new Negation(new UniBox(fref));

		// rewrites
		Formula rwt = new Negation(new MultiBox(iref, fref));
		
		return new AgentSplitRule("EK2", html, f, rwt, context, iref);
	}
	
	public static Rule buildEK3(Context context) {
		String html = "I" + "<sub>" + LOZENGE + "</sub>";
		// variables
		Variable<Formula> F = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(F, context.getFormulaCodeMap().code(F));

		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));

		// formula
		Formula f = new UniDiamond(fref);

		// rewrites
		Formula rwt = new MultiBox(iref, fref);
		
		return new AgentSplitRule("EK3", html, f, rwt, context, iref);
	}
	
	public static Rule buildEK4(Context context) {
		String html = "I" + "<sub>" + LOZENGE + "*</sub>";
		// variables
		Variable<Formula> F = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(F, context.getFormulaCodeMap().code(F));

		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i, context.getAgentCodeMap().code(i));

		// formula
		Formula f = new Negation(new UniDiamond(fref));

		// rewrites
		Formula rwt = new Negation(new MultiBox(iref, fref));
		
		return new AgentLinearRule("EK4", html, f, rwt, context, iref);
	}

	public static Rule buildAnnounce1(Context context) {
		String html = DISJ + "<sub>" + "!" + "</sub>";
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l,
				context.getFormulaCodeMap().code(l));

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r,
				context.getFormulaCodeMap().code(r));

		// formula
		Formula f = new Negation(new Announcement(lref, rref));

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Negation(lref));
		rwt.add(new Conjunction(lref, rref));

		return new LinearRule("Announce1", html, f, rwt);
	}

	public static Rule buildAnnounce2(Context context) {
		String html = DISJ + "<sub>" + "!" + "</sub>";
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l,
				context.getFormulaCodeMap().code(l));

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r,
				context.getFormulaCodeMap().code(r));

		// formula
		Formula f = new Announcement(lref, rref);

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Negation(lref));
		rwt.add(new Conjunction(lref, rref));

		return new SplitRule("Announce2", html, f, rwt);
	}

	public static Rule buildSannounce1(Context context) {
		String html = DISJ + "<sub>" + "?" + "</sub>";
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l,
				context.getFormulaCodeMap().code(l));

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r,
				context.getFormulaCodeMap().code(r));

		// formula
		Formula f = new Negation(new Sannouncement(lref, rref));

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Announcement(lref, new Negation(rref)));

		return new LinearRule("Sannounce1", html, f, rwt);
	}

	public static Rule buildSannounce2(Context context) {
		String html = DISJ + "<sub>" + "?" + "</sub>";
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l,
				context.getFormulaCodeMap().code(l));

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r,
				context.getFormulaCodeMap().code(r));

		// formula
		Formula f = new Sannouncement(lref, rref);

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Negation(new Announcement(lref, new Negation(rref))));

		return new LinearRule("Sannounce2", html, f, rwt);
	}
}
