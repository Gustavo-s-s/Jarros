import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        List<String[][]> reader = reader("caso_teste.txt", new ArrayList<String[][]>());
        List<Jug[][]> build = build(reader);

        int c = 0;
        HashMap<Long, List<Long>> averageMap = new HashMap<>();
        for (var item : build) {
            final JugTree jugTree = new JugTree(item[0], item[1]);
            jugTree.spread();

            if (averageMap.containsKey((long) jugTree.getSteps())) {
                averageMap.get((long) jugTree.getSteps()).add(jugTree.getFinalTime());
            } else {
                ArrayList<Long> values = new ArrayList<>();
                values.add(jugTree.getFinalTime());
                averageMap.put((long) jugTree.getSteps(), values);
            }

            System.out.println("\nExplicação passo a passo: " + jugTree.getLog());
        }

        System.out.println("\nMédia de quantidade de passo / media de tempo de execução");
        for (var key : averageMap.keySet()) {
            System.out.println(key + " " + averageMap.get(key).stream().mapToLong(Long::longValue).average().getAsDouble());
        }
    }

    private static List<String[][]> reader(String path, List<String[][]> list) throws FileNotFoundException {
        File file = new File(path);
        try (Scanner reader = new Scanner(file)) {
            int count = 0;
            String read[][] = new String[3][];
            do {
                String line = reader.nextLine();
                if (line.startsWith("M")) {
                    continue;
                }
                read[count % 3] = line.split(" ");
                count++;
                if (count % 3 == 0) {
                    list.add(read);
                    read = new String[3][];
                }

            } while (reader.hasNextLine());
        }

        return list;
    }

    private static List<Jug[][]> build(List<String[][]> list) {

        List<Jug[][]> jugList = new ArrayList<>();
        for (var item : list) {
            Jug jug = new Jug(Integer.parseInt(item[0][0]), Integer.parseInt(item[1][0]));
            Jug jug1 = new Jug(Integer.parseInt(item[0][1]), Integer.parseInt(item[1][1]));
            Jug jug2 = new Jug(Integer.parseInt(item[0][2]), Integer.parseInt(item[1][2]));
            Jug rjug = new Jug(Integer.parseInt(item[0][0]), Integer.parseInt(item[2][0]));
            Jug rjug1 = new Jug(Integer.parseInt(item[0][1]), Integer.parseInt(item[2][1]));
            Jug rjug2 = new Jug(Integer.parseInt(item[0][2]), Integer.parseInt(item[2][2]));

            Jug jugs[][] = {{jug, jug1, jug2}, {rjug, rjug1, rjug2}};
            jugList.add(jugs);
        }

        return jugList;
    }

}