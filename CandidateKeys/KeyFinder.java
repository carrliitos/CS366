/* @author Benzon Carlitos Salazar
*   CS366 - DBMS
*/

import java.util.*;

public class KeyFinder {

    Map<HashSet<String>, HashSet<String>> fdMap;
    ArrayList<String> attributeList;

    public KeyFinder() {
        this.attributeList = new ArrayList<String>();
        this.fdMap = new HashMap<HashSet<String>, HashSet<String>>();
    }

    public HashSet<String> getCandidateKeys() {

        HashSet<String> keys = new HashSet<String>();
        ArrayList<String> ignoreList = new ArrayList<String>();
        ArrayList<String> combinationList = Main.getCombinations(attributeList);
        Collections.sort(combinationList, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return s1.length() - s2.length();
            }
        });

       // System.out.println("All Combination: " + combinationList);
       // System.out.println("FDs: " + fdMap);
       // System.out.println("Atts: " + attributeList);
        for (String combinationItem : combinationList) {
            boolean stop = false;
            for (String ignore : ignoreList) {
                int found = 0;
                for (Character c : combinationItem.toCharArray()) {
                    //System.out.println(" ------ CHECK " + combinationItem + " : "+ ignore + " : " + c);
                    if (ignore.contains(c.toString())) {
                        found++;
                    }
                }
                if (found == ignore.length()) {
                    stop = true;
                }
            }
            if (stop) {
                continue;
            }

            HashSet<String> atts = new HashSet<String>();
            for (Character C : combinationItem.toCharArray()) {
                atts.add(C.toString());
            }

            HashSet<String> theClosure = atts;
            do {
                theClosure = Main.closure(theClosure, fdMap);
                HashSet<String> closure2 = Main.closure(theClosure, fdMap);
                if (theClosure.size() == closure2.size()) {
                    break;
                } else {
                    theClosure = closure2;
                }
            } while (true);

            System.out.println("Input: "+ atts);
            System.out.println("Closure(Input): "+theClosure);
            if (theClosure.containsAll(attributeList)) {
                //System.out.println("=============> Key: " + combinationItem);
                keys.add(combinationItem);
                ignoreList.add(combinationItem);

            }
        }
        return keys;
    }

    public static StringBuffer sortString(String str) {
        char[] cc = str.toCharArray();
        Arrays.sort(cc);
        return new StringBuffer(new String(cc));
    }

    public void addAttributes(String attributes) {
        String[] attrSplit = attributes.split(",");

        for (String s : attrSplit) {
            attributeList.add(s.substring(0, 1));
        }
    }

    public void addFD(String fds) throws ArrayIndexOutOfBoundsException {
        String[] fdSplit = fds.split(",");

        for (String s : fdSplit) {
            String[] split = s.split("->");
            String rhs = split[1];
            String lhs = split[0];

            // Sort left hand side alphabetically (due to how the HashMap works, AB!=BA)
            lhs = KeyFinder.sortString(lhs).toString();
            HashSet<String> lhsHashSet = new HashSet<String>();
            for (Character C : lhs.toCharArray()) {
                lhsHashSet.add(C.toString());
            }

            // Sort left hand side alphabetically (due to how the HashMap works, AB!=BA)
            HashSet<String> rhsHashSet = new HashSet<String>();
            for (Character C : rhs.toCharArray()) {
                rhsHashSet.add(C.toString());
            }

            fdMap.put(lhsHashSet, rhsHashSet);
        }

    }

    public void reset() {
        attributeList.clear();
        fdMap.clear();
    }
}