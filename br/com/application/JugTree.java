package br.com.application;

import java.util.*;

public class JugTree {
	
	private class JugNode {
		private final Jug jugs[];
		private final Collection<JugNode> children;
		private final StringBuilder builder;
		private int steps;



		public JugNode(Jug jugs[]) {
			this.children = new HashSet<>();
			this.jugs = jugs;
			this.builder = new StringBuilder();
			this.steps = 1;
		}
		
		private boolean containsJugs(Jug otherJugs[]) {

			return Arrays.equals(otherJugs, this.jugs);
		}
		
		private void addChild(JugNode child, String log) {
			child.builder.append(this.builder + "\n");
			child.builder.append(log);
			child.steps = this.steps + 1;
			this.children.add(child);
		}
		
		@Override
		public String toString() {
			return Arrays.toString(this.jugs);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			JugNode jugNode = (JugNode) o;
			return steps == jugNode.steps && Arrays.equals(jugs, jugNode.jugs) && Objects.equals(children, jugNode.children);
		}

		@Override
		public int hashCode() {
			int result = Objects.hash(children, steps);
			result = 31 * result + Arrays.hashCode(jugs);
			return result;
		}
	}
	
	private final JugNode root;
	private final Jug stopTreeJugs[];
	private StringBuilder log;
	private static final long initialTime = System.currentTimeMillis();

	public JugTree(Jug initialJugs[], Jug findJugs[]) {
		this.root = new JugNode(initialJugs);
		this.stopTreeJugs = findJugs;
	}
	
	public Jug[] spread() {
		if (this.root.containsJugs(this.stopTreeJugs))
			return this.stopTreeJugs;
		
		return spread(this.root);
	}
	
	public String log() {
		return this.log.toString();
	}
	
	private Jug[] spread(JugNode currentJugNode) {
		spreader(currentJugNode);
		
		ArrayList<JugNode> passed = new ArrayList<>();
		passed.addAll(currentJugNode.children);
		ArrayList<JugNode> _passed = new ArrayList<>();
		
		Jug jugs[] = null;
		passed.addAll(currentJugNode.children);
		while (jugs == null) {
			for (var child : passed) {
				spreader(child);
				Optional<JugNode> verified = verify(child);
				_passed.addAll(child.children);
				
				if(verified.isPresent()) {
					this.log = verified.get().builder;
					jugs = verified.get().jugs;
					break;
				}
			}

			System.out.println(passed.get(0).steps);
			passed.clear();
			passed.addAll(_passed);
			_passed.clear();
		}
		return jugs;
	}

	private Optional<JugNode> verify(JugNode currentjugNode) {
		JugNode aux = null;
		for (var child : currentjugNode.children) {
			if (child.containsJugs(this.stopTreeJugs)) {
				aux = child;
				break;
			}
		}
		return Optional.ofNullable(aux);
	}
	
	private Jug[] copyArray(Jug jugs[]) {
		final Jug SAVE_JUGS[] = new Jug[jugs.length];
		
		for (int i = 0; i < jugs.length; i++) {
			SAVE_JUGS[i] = jugs[i].copy();
		}
		
		return SAVE_JUGS;
	}
	
	private void spreader(JugNode currentJugNode) {
		for (int i = 0; i < currentJugNode.jugs.length; i++) {
			for (int j = 0; j < currentJugNode.jugs.length; j++) {
				if (i != j) {
					final Jug COPY[] = this.copyArray(currentJugNode.jugs);
					COPY[i].transferWatter(COPY[j]);
					final long finalTime = System.currentTimeMillis();
					String log = String.format("(passo: %d) Jarro %d transfere água para o Jarro %d [%d]", currentJugNode.steps, i+1, j+1,(finalTime-initialTime));
					JugNode node = new JugNode(COPY);
					currentJugNode.addChild(node,log);
				}
			}			
		}
	}
}
