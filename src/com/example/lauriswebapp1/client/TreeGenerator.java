package com.example.lauriswebapp1.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

// NOTE: for this class we assume that
// 1) minimizer always starts; 
// 2) first level contains 6 digits, last 2;
// 3) digit count reduces by 1 on every move (observation)

public class TreeGenerator {
	private String rootNumber;
	private GameTree tree;
	public static final int MIN_NUM_LENGTH = 2;
	
	private void generateChildren(String child, String parent) {
		// Pievieno apakšstāvokli stāvokļu kokam.
		tree.addNode(child, parent);
		// Turpina pievienot apakšstāvokļa apakšstāvokļus, utt.
		generateChildren(child);
	}
	
	// Rekursīvi izveido norādītajai virsotnei visus
	// apakšstāvokļus.
	private void generateChildren(String number) {
		// Strupceļa virsotne nav apakšstāvokļu
		if(number.length() <= MIN_NUM_LENGTH) {
			return;
		}
		
		// Izveido stāvokļus, kas seko, ja noņem pēdējo ciparu.
		if(number.length() % 2 != 0) {
			generateChildren(number.substring(0, number.length() - 1),
					number);
		}
		
		// Izveido stāvokļus, kas seko, ja saskaita 1. un 2. ciparu.
		generateChildren(TreeGenerator.joinDigits(number, 0, 1) + 
				number.substring(2), number);
		
		// Izveido stāvokļus, kas seko, ja saskaita 3. un 4. ciparu.
		if(number.length() > 3) {
			generateChildren(number.substring(0, 2) + 
					TreeGenerator.joinDigits(number, 2, 3) + 
					number.substring(4), number);
		}
		
		// Izveido stāvokļus, kas seko, ja saskaita 5. un 6. ciparu.
		if(number.length() > 5) {
			generateChildren(number.substring(0, 4) + 
					TreeGenerator.joinDigits(number, 4, 5), number);
		}
	}
	
	// Nosaka strupceļa virsotnes heiristisko vērtējumu
	private int getEndMinMaxValue(String number) {
		int minMaxValue = 0;
		if(number.charAt(0) > number.charAt(1)) {
			minMaxValue = 1;
		}
		else if(number.charAt(0) < number.charAt(1)) {
			minMaxValue = -1;
		}
		return minMaxValue;
	}
	
	// Aizpilda spēles kokā minmax vērtības.
	// Darbojas rekursīvi paņemot heiristiskās vērtības
	// no strupceļa virstonēm.
    private void fillMinMaxValues(String number) {
    	TreeNode curNode = tree.getNodes().get(number);
    	// Ja nav -2, tad ir jau izskaitļota minmax vērtība
    	if(curNode.getMinMaxValue() != -2) {
    		return;
    	}
    	// Strupceļa virsotne, uztādām vertību atkarībā no skaitļa cipariem.
    	// Izejam no procedūras, kas ļauj novirzīt vērtējumus augstākos līmeņos.
    	if(number.length() <= MIN_NUM_LENGTH) {
    		curNode.setMinMaxValue(getEndMinMaxValue(number));
    		return;
    	}
    	
    	// Šī nav strupceļa virsotne, vispirms aizpildam
    	// apakšvirsotņu vērtējumus.
    	ArrayList<String> children = curNode.getChildren();
    	for(String child : children) {
    		fillMinMaxValues(child);
    	}
    	
    	// Tagad atrodam vērtējumu pašreizējai virsotnei.
    	ArrayList<Integer> minMaxValues = new ArrayList<>();
    	for(String child : children) {
    		minMaxValues.add(tree.getNodes().get(child).getMinMaxValue());
    	}
    	// Izvēlamies mazāko vai lielāko vērtību atkarībā no līmeņa.
    	if(number.length() % 2 == 0) {
    		curNode.setMinMaxValue(Collections.min(minMaxValues));
    	}
    	else {
    		curNode.setMinMaxValue(Collections.max(minMaxValues));
    	}
    }
    
    // Ģenerē uzvaras ceļus. Katrs ceļs ir saraksts ar stāvokļiem.
    // Saņem spēles stāvokli number, kā arī pathMinMaxValue, kādi vērtējumi
    // būs visām uzvaras ceļu virsotnēm.
    private ArrayList<ArrayList<String>> getWinningPaths(String number, int pathMinMaxValue, 
    		ArrayList<ArrayList<String>> curWinningPaths) {
    	// Glabā ceļus, kas tiks pievienoti šajā iterācijā.
    	ArrayList<ArrayList<String>> newPaths = new ArrayList<>();
    	// Glabā ceļus, kurus neizdodas turpināt kā uzvaras ceļus.
    	// Tos vajag atmest.
    	ArrayList<ArrayList<String>> badPaths = new ArrayList<>();
    	// Sākotnēji curWinningPaths satur tikai saknes virsotni.
    	// No tās tiek ņemti visi ceļi, kuriem sakrīt vērtējums.
    	for(ArrayList<String> path : curWinningPaths) {
    		String endNode = path.get(path.size() - 1);
    		boolean pathContinued = false;
    		// Veic ceļu turpināšanu piesaistot pēctečus, ja tiem
    		// ir atbilstoši  vērtējumi
    		ArrayList<String> children = 
    				tree.getNodes().get(endNode).getChildren();
    		for(String child : children) {
	    		if(tree.getNodes().get(child).getMinMaxValue() 
	    				== pathMinMaxValue) {
	    			ArrayList<String> pathCopy = 
	    					new ArrayList<>(path);
	    			pathCopy.add(child);
	    			newPaths.add(pathCopy);
	    			pathContinued = true;
	    		}
    		}
    		// Pievieno ceļu tiem, kurus būs jāatmet.
    		if(!pathContinued && children.size() > 0) {
    			badPaths.add(path);
    		}
    	}
		// Atmet ceļus, kas nav uzvaras.
    	curWinningPaths.removeAll(badPaths);
    	if(newPaths.size() > 0) {
    		// Turpina uzvaras ceļus liekot klāt jaunus pēctečus
    		curWinningPaths = getWinningPaths(number, pathMinMaxValue, newPaths);
    	}
    	return curWinningPaths;
    }
    
	public static String joinDigits(String str, int idx1, int idx2) {
		int sum = (str.charAt(idx1)-48)+(str.charAt(idx2)-48);		
		if(sum > 6) {
			sum = (12-sum)+1;
		}
		return String.valueOf(sum);
	}
	
	// Constructor
	public TreeGenerator(String rootNumber) {
		this.rootNumber = rootNumber;
	}
	
	// Properties
	public GameTree getTree() {
		return tree;
	}
	
	// Public interface
	public void generate() {
		tree = new GameTree();  // Izveidojam tukšu spēles koku
		tree.addNode(rootNumber);  // Pievienojam saknes virsotni
		generateChildren(rootNumber);  // Piesaistam saknes virsotnei rekursīvi pārējās virsotnes
	}
	
	public void fillMinMaxValues() {
		fillMinMaxValues(this.rootNumber);
	}
	
	public ArrayList<ArrayList<String>> getWinningPaths(String number) {
		ArrayList<ArrayList<String>> paths = new ArrayList<>();
		paths.add(new ArrayList<>(Arrays.asList(number)));
		return getWinningPaths(number, tree.getNodes().get(number).getMinMaxValue(), paths);
	}
}
