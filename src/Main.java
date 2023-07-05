import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] parts = reader.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        int q = Integer.parseInt(parts[2]);
        long[] reset = new long[n]; // количество перезапусков дата-центров
        long[] ra = new long[n];
        Map<Integer, Set<Integer>> badServers = new HashMap<>();
        TreeMap<Long, TreeSet<Integer>> mapRA = new TreeMap<>();
        Map<Long, Integer> mapMinMax = new TreeMap<>();
        TreeSet<Integer> list = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            reset[i] = 0;
            ra[i] = 0;
            badServers.put(i, new HashSet<>());
            list.add(i + 1);
        }
        mapRA.put(0L, list);
        mapMinMax.put(0L, 1);
        StringBuilder sb = new StringBuilder();
        long maxRA = 0;
        for (int i = 0; i < q; i++) {
            parts = reader.readLine().split(" ");
            int num = 0, serv = 0;
            boolean needCount = false;
            switch (parts[0]) {
                case "RESET" -> {
                    num = Integer.parseInt(parts[1]);
                    reset[num - 1]++;
                    Set<Integer> set = badServers.get(num - 1);
                    set.clear();
                    badServers.put(num - 1, set);
                    needCount = true;
                    break;
                }
                case "DISABLE" -> {
                    num = Integer.parseInt(parts[1]);
                    serv = Integer.parseInt(parts[2]);
                    Set<Integer> set = badServers.get(num - 1);
                    if (!set.contains(serv)) {
                        set.add(serv);
                        needCount = true;
                    }
                    break;
                }
                case "GETMAX" -> {
                    sb.append(mapMinMax.get(maxRA));
                    sb.append("\n");
                    continue;
                }
                case "GETMIN" -> {
                    long minRA = mapRA.firstKey();
                    sb.append(mapMinMax.get(minRA));
                    sb.append("\n");
                    continue;
                }
            }
            if (needCount) {
                long tempRA = reset[num - 1] * (m - badServers.get(num - 1).size());
                long last = ra[num - 1];
                if (last == tempRA) {
                    continue;
                }
                if (tempRA > maxRA) {
                    maxRA = tempRA;
                }
                ra[num - 1] = tempRA;
                TreeSet<Integer> setLast = mapRA.get(last);
                setLast.remove(num);
                boolean needMax = false;
                if (setLast.isEmpty()) {
                    mapRA.remove(last);
                    mapMinMax.remove(last);
                    if (last == maxRA) {
                        needMax = true;
                    }
                } else {
                    int minLast = mapMinMax.get(last);
                    if (minLast == num) {
                        minLast = setLast.first();
                        mapMinMax.put(last, minLast);
                    }
                }
                TreeSet<Integer> setCur = mapRA.get(tempRA);
                if (setCur == null) {
                    setCur = new TreeSet<>();
                }
                setCur.add(num);
                mapRA.put(tempRA, setCur);

                if (needMax) {
                    maxRA = mapRA.lastKey();
                }
                int minCur = mapMinMax.getOrDefault(tempRA, num);
                if (num < minCur) {
                    minCur = num;
                }
                mapMinMax.put(tempRA, minCur);
            }
        }
        System.out.println(sb);
    }
}

