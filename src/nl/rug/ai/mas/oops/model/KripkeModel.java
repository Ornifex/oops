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

package nl.rug.ai.mas.oops.model;

import nl.rug.ai.mas.oops.formula.AgentId;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;

public class KripkeModel {
	Set<AgentId> d_agents;
	HashMap<AgentId, DefaultDirectedGraph<World, Arrow>> d_graphs;

	public KripkeModel(Set<AgentId> agents) {
		System.out.println("Agents: " + agents);
		d_agents = agents;
		d_graphs = new HashMap<AgentId, DefaultDirectedGraph<World, Arrow>>();
		for (AgentId a : d_agents) {
			d_graphs.put(a,
				new DefaultDirectedGraph<World, Arrow>(new DummyEdgeFactory()));
		}
	}

	/**
	 * @return true if the World was not already in the model.
	 */
	public boolean addWorld(World w) {
		for (DefaultDirectedGraph<World, Arrow> graph :
				d_graphs.values()) {
			if (!graph.addVertex(w)) { // world already exists
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true if the Arrow was not already in the model.
	 */
	public boolean addArrow(Arrow r) {
		System.out.println("Adding " + r.getAgent() + "=(" + r.getSource() +
			"," + r.getTarget() + ")");
		DefaultDirectedGraph<World, Arrow> g = d_graphs.get(r.getAgent());
		return g.addEdge(r.getSource(), r.getTarget(), r);
	}

	public boolean addArrow(AgentId agent, World source, World target) {
		return addArrow(new Arrow(agent, source, target));
	}

	public Set<Arrow> getArrows(AgentId agent, World source) {
		Set<Arrow> arrows = new HashSet<Arrow>();
		System.out.println("getArrows " + agent + ":(" + source + ",?): " +
			d_graphs.get(agent).edgesOf(source));
		for (Arrow r : d_graphs.get(agent).edgesOf(source)) {
			if (r.getSource().equals(source)) {
				arrows.add(r);
			}
		}
		System.out.println(arrows);
		return arrows;
	}

	public Set<World> getWorlds() {
		return d_graphs.get(d_agents.iterator().next()).vertexSet();
	}

	public DirectedMultigraph<World, Arrow> constructMultigraph() {
		DirectedMultigraph<World, Arrow> graph =
			new DirectedMultigraph<World, Arrow>(new DummyEdgeFactory());

		for (World w : getWorlds()) {
			graph.addVertex(w);
		}

		for (DefaultDirectedGraph<World, Arrow> g : d_graphs.values()) {
			for (Arrow r : g.edgeSet()) {
				graph.addEdge(r.getSource(), r.getTarget(), r);
			}
		}

		return graph;
	}

	public String toString() {
		String str = this.getClass().getSimpleName() + ":\n";
		for (Map.Entry<AgentId, DefaultDirectedGraph<World, Arrow>> e :
				d_graphs.entrySet()) {
			str += e.getKey().toString() + " -> " + e.getValue().toString() +
				"\n";
		}
		return str;
	}

	private class DummyEdgeFactory implements EdgeFactory<World, Arrow> {
		public Arrow createEdge(World s, World t) {
			return null;
		}
	}
}
