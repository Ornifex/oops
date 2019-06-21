package nl.rug.ai.mas.oops.tableau;

import nl.rug.ai.mas.oops.formula.*;

public class MultiModalValidator implements FormulaValidator {
	private static class Validator implements FormulaVisitor {
		private boolean d_valid = true;

		public void visitBiImplication(BiImplication f) {		}

		public void visitConjunction(Conjunction f) {		}

		public void visitDisjunction(Disjunction f) {		}

		public void visitImplication(Implication f) {		}

		public void visitMultiBox(MultiBox f) {		}

		public void visitMultiDiamond(MultiDiamond f) {		}

		public void visitNegation(Negation f) {		}

		public void visitProposition(Proposition f) {		}

		public void visitUniBox(UniBox f) {
			d_valid = false;
		}

		public void visitUniDiamond(UniDiamond f) {
			d_valid = false;
		}

		public void visitAnnouncement(Announcement f) { d_valid = false; }

		public void visitSannouncement(Sannouncement f) { d_valid = false; }

		public void visitFormulaReference(FormulaReference f) {		}

		public boolean getResult() {
			return d_valid;
		}
	}

	public boolean validate(Formula f) {
		Validator v = new Validator();
		f.accept(v);
		return v.getResult();
	}

	public String toString() {
		return "MultiModalValidator";
	}
}
