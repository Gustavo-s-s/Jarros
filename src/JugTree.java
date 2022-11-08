

import java.util.*;

public class JugTree { // arvore

    private class JugNode { // classe folhas
        private final Jug jugs[]; // vazos dentro da folha
        private final Collection<JugNode> children; // filhos da folha
        private final StringBuilder builder; // log da folha
        private int steps; // quantidade de passos
        private long time;


        public JugNode(Jug jugs[]) { // costrutor
            this.children = new HashSet<>();
            this.jugs = jugs;
            this.builder = new StringBuilder();
            this.steps = 1;
            this.time = 0;
        }

        private boolean containsJugs(Jug otherJugs[]) { // verifica se há um determinado array de jarro na folha
            return Arrays.equals(otherJugs, this.jugs);
        }

        private void addChild(JugNode child, String log, long finalTime) { // adiciona um  filho à folha
            child.builder.append(this.builder + "\n");
            child.builder.append(log);
            child.steps = this.steps + 1;
            child.time = finalTime - this.time;
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

    private final JugNode root; // raiz
    private final Jug stopTreeJugs[]; // jarro que queremos achar dentro da das posibilidades
    private StringBuilder log; // log final
    private final long initialTime = System.currentTimeMillis(); // tempo inicial
    private long finalTime; // tempo final realizado para executar
    private int steps; // quantidade de passos

    public JugTree(Jug initialJugs[], Jug findJugs[]) { // construtor
        this.root = new JugNode(initialJugs);
        this.stopTreeJugs = findJugs;
    }

    public void spread() { // espalha as posibilades
        if (this.root.containsJugs(this.stopTreeJugs))
            return; // sai do programa se não há movimentos para fazer

        spread(this.root); // entra em uma sobrecarga de spread
    }

    public String getLog() {
        return this.log.toString();
    }

    public long getFinalTime() {
        return finalTime;
    }

    public int getSteps() {
        return steps-1;
    }

    private void spread(JugNode currentJugNode) { // espalha a arvore
        spreader(currentJugNode); // entra no espalhador da arvore
        Optional<JugNode> verify = verify(currentJugNode); // verifica se o filho que queremos está nos filhos do node gerado

        if (verify.isPresent()) { // se o filho que queremos esiver o retornamos
            this.finalTime = verify.get().time;
            this.steps = verify.get().steps;
            this.log = verify.get().builder;
            return;
        }

        HashSet<JugNode> passed = new HashSet<>(); // folhas que irão ser passadas
        passed.addAll(currentJugNode.children); // adiciona todas as folhas
        HashSet<JugNode> _passed = new HashSet<>(); // folhas que já foram passadas

        boolean run = true;
        while (run) { // enquanto não tiver o filho queremos
            for (var child : passed) { // para cada filho dentro de passed
                spreader(child); // espalha
                Optional<JugNode> verified = verify(child); // verifica se contem o filho que queremos
                _passed.addAll(child.children); // adiciona os filhos dos filhos para proxima iteração

                /*
                 *
                 * se (se a sequencia de jarros for encotrada) {
                 *	para
                 * }
                 *
                 * */
                if(verified.isPresent()) {
                    this.finalTime = verified.get().time;
                    this.steps = verified.get().steps;
                    this.log = verified.get().builder;
                    run = false;
                    break;
                }
            }


            passed.clear(); // apaga todos os filhos que já foram passados
            passed.addAll(_passed); // adiciona a nova geração defolhas para serem verificadas
            _passed.clear(); // limpa a geração antiga
        }
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
                    this.finalTime = finalTime - this.initialTime;
                    String log = String.format("%d %d", currentJugNode.steps,this.finalTime);
                    JugNode node = new JugNode(COPY);
                    currentJugNode.addChild(node, log, this.finalTime);
                }
            }
        }
    }
}
