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

package nl.rug.ai.mas.oops.formula;

public interface Formula {
	/**
	 * Attempt to match this formula to another formula, returning a
	 * substitution for the variables in this formula.
	 */
    FullSubstitution match(Formula f);
	/**
	 * Substitute for all variables the values in the substitution s.
	 */
    Formula substitute(FullSubstitution s);
	/**
	 * Accept a Visitor to this formula (Visitor pattern).
	 */
    void accept(FormulaVisitor v);
	/**
	 * Return the simplest opposite for the formula
	 */
    Formula opposite();
	/**
	 * Return wether the formula is simple (an atom or a negation of an atom).
	 */
    boolean isSimple();
	/**
	 * @return true if the formula contains no free variables, false otherwise
	 */
    boolean isConcrete();
	/**
	 * @return The code number for this Formula.
	 */
    int code();
}
