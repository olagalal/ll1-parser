package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import javafx.util.Pair;

public class LL1 {

    public static boolean CheckLL1(String[] L, String[] R) {
        boolean f = true;

        //Check if grammar is left recursion
        for (int i = 0; i < L.length; i++) {
            for (int j = 0; j < L.length; j++) {
                if (i == j) {
                    continue;
                }

                if (L[i].equals(L[j])) {
                    if ((R[i].charAt(0)) == (L[i].charAt(0)) && (R[j].charAt(0)) != (L[j].charAt(0))) {
                        f = false;
                        break;
                    }
                }

            }
        }

        //Check if the same nonterminal starts with the same terminal (deterministic)
        for (int i = 0; i < L.length; i++) {
            for (int j = 0; j < L.length; j++) {
                if (i == j) {
                    continue;
                }

                if (L[i].equals(L[j])) {
                    if ((R[i].charAt(0)) == (R[j].charAt(0))) {
                        f = false;
                        break;
                    }
                }
            }
        }
        return f;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the numbers of rules: ");
        int N = s.nextInt();

        String tmp = "";
        System.out.println("Enter the Start Symbol:");
        String startSymbol = s.next();

        String[] LHS = new String[N];
        String[] RHS = new String[N];
        for (int i = 0; i < N; i++) {
            System.out.println("Enter the left hand side of rule " + (i + 1));
            LHS[i] = s.next();

            System.out.println("Enter the right hand side of rule " + (i + 1));
            tmp = s.next();
            if (tmp.equals("phi")) {
                RHS[i] = "\u03D5";
            } else {
                RHS[i] = tmp;
            }
        }

        //print rules
        System.out.println("");
        System.out.println("Set of Rules");
        for (int i = 0; i < N; i++) {
            System.out.println(LHS[i] + " \u2192 " + RHS[i]);
        }

        //check if they are LL(1)
        System.out.println("");
        boolean flag = CheckLL1(LHS, RHS);
        if (flag) {
            System.out.println("Grammar is LL(1) Grammar");
        } else {
            System.out.println("Grammar is not LL(1) Grammar");
            System.exit(0);
        }

        //Print Set of Terminals and Nonterminals:
        Set<Character> nonterminal = new HashSet();
        Set<Character> terminal = new HashSet();

        for (int i = 0; i < N; i++) {
            nonterminal.add(LHS[i].charAt(0));
            int x = RHS[i].length();
            for (int j = 0; j < x; j++) {
                if (RHS[i].charAt(j) >= 97 && RHS[i].charAt(j) <= 122) {
                    terminal.add(RHS[i].charAt(j));
                }
            }
        }

        System.out.println("");
        System.out.println("Non-Terminals: " + nonterminal);
        System.out.println("Terminals: " + terminal);

        //Step1: Find all nullable rules and nullable nonterminals
        System.out.println("");
        System.out.println("Step1:");
        ArrayList nullRules = new ArrayList();
        for (int i = 0; i < N; i++) {
            if (RHS[i].equals("\u03D5")) {
                nullRules.add(i);
            }
        }
        if (nullRules.isEmpty()) {
            System.out.println("\nNo Nullable R0ules\nNoNullable nonterminals\n");
        } else {
            for (int i = 0; i < nullRules.size(); i++) {
                System.out.print("Nullable rule: " + ((int) nullRules.get(i) + 1) + "\nNullable nonterminal: " + LHS[(int) nullRules.get(i)] + "\n");
            }

        }

        //Step2: Compute the relation BDW for each non-terminal
        ArrayList<String> L = new ArrayList<String>(Arrays.asList(LHS));
        ArrayList<String> R = new ArrayList<String>(Arrays.asList(RHS));

        System.out.println("");
        System.out.println("Step2:");
        Pair e, r;
        Set<Pair<Character, Character>> BDW = new HashSet();
        for (int i = 0; i < L.size(); i++) {
            for (int j = 0; j < nullRules.size(); j++) {
                if (i == (int) nullRules.get(j)) {
                    for (int k = 0; k < L.size(); k++) {
                        if ((R.get(k).charAt(0)) == (L.get(i).charAt(0))) {
                            L.add(L.get(k));
                            String tmp2 = R.get(k).substring(R.get(k).indexOf(R.get(k).charAt(0)) + 1);
                            if (tmp2.equals("")) {
                                R.add("\u03D5");
                            } else {
                                R.add(tmp2);
                            }
                            if (k > i) {
                                break;
                            }
                            continue;
                        }
                    }
                }
            }

            if (R.get(i).equals("\u03D5")) {
                continue;
            }

            e = new Pair(L.get(i).charAt(0), R.get(i).charAt(0));
            BDW.add(e);
            System.out.println(e.getKey() + " BDW " + e.getValue());
        }
        /*
        System.out.println("");
        for (int i = 0; i < L.size(); i++) {
            System.out.print(L.get(i) + " ");
            System.out.print(R.get(i) + "\n");
        }
         */

        //Step3: Compute the relation BW
        ArrayList<Character> R_BW = new ArrayList<>();
        ArrayList<Character> L_BW = new ArrayList<>();

        System.out.println("");
        System.out.println("Step3:");
        Iterator<Pair<Character, Character>> itr = BDW.iterator();
        while (itr.hasNext()) {
            e = itr.next();
            L_BW.add((Character) e.getKey());
            R_BW.add((Character) e.getValue());
        }

        for (int i = 0; i < L_BW.size(); i++) {
            for (int j = 0; j < L_BW.size(); j++) {
                if (i == j) {
                    continue;
                }

                if (R_BW.get(i) == L_BW.get(j)) {
                    L_BW.add(L_BW.get(i));
                    R_BW.add(R_BW.get(j));
                }
            }
        }

        Iterator<Character> itr2 = nonterminal.iterator();
        while (itr2.hasNext()) {
            char x = itr2.next();
            L_BW.add(x);
            R_BW.add(x);
        }

        Iterator<Character> itr3 = terminal.iterator();
        while (itr3.hasNext()) {
            char x = itr3.next();
            L_BW.add(x);
            R_BW.add(x);
        }

        for (int i = 0; i < L_BW.size(); i++) {
            System.out.println(L_BW.get(i) + " BW " + R_BW.get(i));
        }

        //Step4: Compute First for each symbol
        System.out.println("");
        System.out.println("Step4:");
        Set<Pair<Character, Character>> First = new HashSet<>();
        Iterator<Character> itr4 = nonterminal.iterator();

        while (itr4.hasNext()) {
            char x = itr4.next();
            for (int i = 0; i < L_BW.size(); i++) {
                if (L_BW.get(i) == x && Character.isLowerCase(R_BW.get(i))) {
                    e = new Pair(L_BW.get(i), R_BW.get(i));
                    First.add(e);
                }
            }
        }

        Iterator<Character> itr5 = terminal.iterator();
        while (itr5.hasNext()) {
            char x = itr5.next();
            for (int i = 0; i < terminal.size(); i++) {
                e = new Pair(x, x);
                First.add(e);
            }
        }

        Iterator<Pair<Character, Character>> itr6 = First.iterator();
        e = itr6.next();
        System.out.print("First(" + e.getKey() + ") = [" + e.getValue());
        while (itr6.hasNext()) {
            r = itr6.next();
            if (r.getKey() == e.getKey()) {
                System.out.print(", " + r.getValue());
            } else {
                System.out.print("]\nFirst(" + r.getKey() + ") = [" + r.getValue());
            }
            e = r;
        }
        System.out.print("]\n");

        //Step5: Compute First of right side of each rule
        Set<Pair<Character, Character>> FirstRHS = new HashSet<>();

        System.out.println("");
        System.out.println("Step5:");
        for (int i = 0; i < RHS.length; i++) {
            if (RHS[i].equals("\u03D5")) {
                e = new Pair(RHS[i], " ");
                FirstRHS.add(e);
            }

            Iterator<Pair<Character, Character>> itr7 = First.iterator();
            while (itr7.hasNext()) {
                r = itr7.next();
                for (int j = 0; j < RHS[i].length(); j++) {
                    if ((char) r.getKey() == RHS[i].charAt(j)) {
                        e = new Pair(RHS[i], (char) r.getValue());
                        FirstRHS.add(e);
                    }
                }
            }
        }

        Iterator<Pair<Character, Character>> itr8 = FirstRHS.iterator();
        e = itr8.next();
        System.out.print("First(" + e.getKey() + ") = [" + e.getValue());
        while (itr8.hasNext()) {
            r = itr8.next();
            if (r.getKey() == e.getKey()) {
                System.out.print(", " + r.getValue());
            } else {
                System.out.print("]\nFirst(" + r.getKey() + ") = [" + r.getValue());
            }
            e = r;
        }
        System.out.print("]\n");

        Set<Pair<Character, Character>> Fol = new HashSet<>();

        if (!nullRules.isEmpty()) {
            //Step6: Compute Followed Directly By
            System.out.println("");
            System.out.println("Step6:");
            Set<Pair<Character, Character>> FDB = new HashSet<>();
            for (int i = 0; i < RHS.length; i++) {
                for (int j = 0; j < RHS[i].length(); j++) {
                    if (Character.isUpperCase(RHS[i].charAt(j)) && j + 1 < RHS[i].length()) {
                        e = new Pair(RHS[i].charAt(j), RHS[i].charAt(j + 1));
                    }
                    FDB.add(e);
                }
            }

            Iterator<Pair<Character, Character>> itr9 = FDB.iterator();
            while (itr9.hasNext()) {
                r = itr9.next();
                System.out.println(r.getKey() + " FDB " + r.getValue());
            }

            //Step7: Compute is Direct End Of
            System.out.println("");
            System.out.println("Step7:");
            Set<Pair<Character, Character>> DEO = new HashSet<>();
            for (int i = 0; i < RHS.length; i++) {
                if (RHS[i].charAt(RHS[i].length() - 1) == '\u03D5') {
                    continue;
                }

                for (int j = 0; j < nullRules.size(); j++) {
                    if (RHS[i].endsWith(LHS[(int) nullRules.get(j)])) {
                        e = new Pair(RHS[i].charAt(RHS[i].length() - 1), LHS[i].charAt(0));
                        DEO.add(e);
                        e = new Pair(RHS[i].charAt(RHS[i].length() - 2), LHS[i].charAt(0));
                        DEO.add(e);
                    } else {
                        e = new Pair(RHS[i].charAt(RHS[i].length() - 1), LHS[i].charAt(0));
                        DEO.add(e);
                    }
                }

            }

            Iterator<Pair<Character, Character>> itr10 = DEO.iterator();
            while (itr10.hasNext()) {
                r = itr10.next();
                System.out.println(r.getKey() + " DEO " + r.getValue());
            }

            //Step8: Compute the relation Is End Of
            ArrayList<Character> R_EO = new ArrayList<>();
            ArrayList<Character> L_EO = new ArrayList<>();

            System.out.println("");
            System.out.println("Step8:");
            Iterator<Pair<Character, Character>> itr11 = DEO.iterator();
            while (itr11.hasNext()) {
                e = itr11.next();
                if (L_EO.contains((Character) e.getKey()) && R_EO.contains((Character) e.getValue())) {
                    if (L_EO.indexOf((Character) e.getKey()) != R_EO.indexOf((Character) e.getValue())) {

                    }
                } else {
                    L_EO.add((Character) e.getKey());
                    R_EO.add((Character) e.getValue());
                }
            }

            for (int i = 0; i < L_EO.size(); i++) {
                for (int j = 0; j < L_EO.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                                        
                    if (R_EO.get(i) == L_EO.get(j)) {
                        if (L_EO.contains(L_EO.get(i)) && R_EO.contains(R_EO.get(j))) {
                            if (L_EO.indexOf(L_EO.get(i)) != R_EO.indexOf(R_EO.get(j))) {

                            }
                        } else {
                            L_EO.add(L_EO.get(i));
                            R_EO.add(R_EO.get(j));
                        }
                    }
                    
                    if (j > i) {
                        break;
                    }
                    continue;
                }
            }

            Iterator<Character> itr12 = nonterminal.iterator();
            while (itr12.hasNext()) {
                char x = itr12.next();
                if (L_EO.contains(x) && R_EO.contains(x)) {
                    if (L_EO.indexOf(x) != R_EO.indexOf(x)) {

                    }
                } else {
                    L_EO.add(x);
                    R_EO.add(x);
                }
            }

            Iterator<Character> itr13 = terminal.iterator();
            while (itr13.hasNext()) {
                char x = itr13.next();
                if (L_EO.contains(x) && R_EO.contains(x)) {
                    if (L_EO.indexOf(x) != R_EO.indexOf(x)) {

                    }
                } else {
                    L_EO.add(x);
                    R_EO.add(x);
                }
            }

            for (int i = 0; i < L_EO.size(); i++) {
                System.out.println(L_EO.get(i) + " EO " + R_EO.get(i));
            }

            //Step9: Compute the relation followed by
            System.out.println("");
            System.out.println("Step:9");
            Set<Pair<Character, Character>> FB = new HashSet<>();
            for (int i = 0; i < L_EO.size(); i++) {
                Iterator<Pair<Character, Character>> itr14 = FDB.iterator();
                while (itr14.hasNext()) {
                    r = itr14.next();
                    for (int k = 0; k < L_BW.size(); k++) {
                        if (R_EO.get(i) == r.getKey() && r.getValue() == L_BW.get(k)) {
                            e = new Pair(L_EO.get(i), R_BW.get(k));
                            FB.add(e);
                        }
                    }
                }
            }

            Iterator<Pair<Character, Character>> itr15 = FB.iterator();
            while (itr15.hasNext()) {
                r = itr15.next();
                System.out.println(r.getKey() + " FB " + r.getValue());
            }

            //Step10: Extend the FB relation to include endmarker
            System.out.println("");
            System.out.println("Step10:");
            for (int i = 0; i < L_EO.size(); i++) {
                if (R_EO.get(i) == startSymbol.charAt(0) && Character.isUpperCase(L_EO.get(i))) {
                    System.out.println(L_EO.get(i) + " FB " + "\u21B2" + " because " + L_EO.get(i) + " EO " + startSymbol);
                    e = new Pair(startSymbol, '\u21B2');
                    FB.add(e);
                }
            }

            //Step11: Compute the Follow Set for each nullable nonterminal
            System.out.println("");
            System.out.println("Step11:");
            for (int i = 0; i < nullRules.size(); i++) {
                Iterator<Pair<Character, Character>> itr16 = FB.iterator();
                while (itr16.hasNext()) {
                    r = itr16.next();
                    if (LHS[(int) nullRules.get(i)].equals(r.getKey().toString()) && (char) r.getValue() >= 97 && (char) r.getValue() <= 120) {
                        e = new Pair(r.getKey(), r.getValue());
                        Fol.add(e);
                    }
                }
            }

            Iterator<Pair<Character, Character>> itr17 = Fol.iterator();
            e = itr17.next();
            System.out.print("Fol(" + e.getKey() + ") = [" + e.getValue());
            while (itr17.hasNext()) {
                r = itr17.next();
                if (r.getKey() == e.getKey()) {
                    System.out.print(", " + r.getValue());
                } else {
                    System.out.print("]\nFol(" + r.getKey() + ") = [" + r.getValue());
                }
            }
            System.out.print("]\n");

        }

        //Step12: Compute the selection set dor each rule
        System.out.println("");
        System.out.println("Step12:");
        Set<Pair<Integer, Character>> Sel = new HashSet<>();
        for (int i = 0; i < N; i++) {
            if (RHS[i] == "\u03D5") {
                //Nullable Rule > Sel(i) = First(alpha) U Fol(A)
                Iterator<Pair<Character, Character>> itr19 = Fol.iterator();
                while (itr19.hasNext()) {
                    r = itr19.next();
                    if (LHS[i].charAt(0) == ((char) r.getKey())) {
                        e = new Pair(i, r.getValue());
                        Sel.add(e);
                    }
                }
            } else {
                Iterator<Pair<Character, Character>> itr20 = FirstRHS.iterator();
                while (itr20.hasNext()) {
                    r = itr20.next();
                    if (RHS[i] == r.getKey()) {
                        e = new Pair(i, r.getValue());
                        Sel.add(e);
                    }
                }
            }
        }

        Iterator<Pair<Integer, Character>> itrFINAL = Sel.iterator();
        e = itrFINAL.next();
        System.out.print("Sel(" + ((int) e.getKey() + 1) + ") = [" + e.getValue());
        while (itrFINAL.hasNext()) {
            r = itrFINAL.next();
            if (r.getKey() == e.getKey()) {
                System.out.print(", " + r.getValue());
            } else {
                System.out.print("]\nSel(" + ((int) r.getKey() + 1) + ") = [" + r.getValue());
            }
            e = r;
        }
        System.out.print("]\n");

    }
}
