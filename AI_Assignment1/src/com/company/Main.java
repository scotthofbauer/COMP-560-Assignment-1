package com.company;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    private static LinkedList<Node> coloredNodes = new LinkedList<Node>();
    private static int count;


    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        StringBuilder sb = new StringBuilder();
        int blank_line_count = 0;
        Node map[] = new Node[48];
        int count = 0;
        String line = br.readLine();
        while (line != null && blank_line_count<3) {
            if(line.length() != 0){

                if(blank_line_count == 1){
                    map[count] = new Node((String) line);
//                    System.out.println("Added state: " + map[count].get_name());
                    count++;
                }else if(blank_line_count == 2){
                    String[] strs = line.trim().split("\\s+");
                    Node state1 = findState(map, strs[0]);
                    Node state2 = findState(map, strs[1]);
                    state1.addNeighbor(state2);
                    state2.addNeighbor(state1);
                }
            }else{
                blank_line_count++;
            }

            line = br.readLine();
        }
//	    for(int i = 0; i<map.length; i++){
//	    	map[i].printNeighbors();
//	    }
//	    Node ky = findState(map, "KY");
//	    ky.printNeighbors();
//	    Node in = findState(map, "IN");
//	    in.printNeighbors();
//
//	    ky.addNode();
//	    in.addNode();
//	    System.out.println("/////////");
//	    ky = findState(map, "KY");
//	    ky.printNeighbors();
//	    in = findState(map, "IN");
//	    in.printNeighbors();

//        colorStates(map);
        localSearchSolution(map);
        System.out.println("///////////////");
//        System.out.println("Colored " + getCount() + " states!");
//	    Node az = findState(map, "AZ");
//	    az.printNeighbors();
//	    Node ca = findState(map, "CA");
//	    ca.printNeighbors();
//	    Node or = findState(map, "OR");
//	    or.printNeighbors();
//	    Node wa = findState(map, "WA");
//	    wa.printNeighbors();
//	    Node ut = findState(map, "UT");
//	    ut.printNeighbors();
//	    Node nv = findState(map, "NV");
//	    nv.printNeighbors();
//	    Node id = findState(map, "ID");
//	    id.printNeighbors();
//	    for(int i = 0; i<map.length; i++){
//	    	map[i].printNeighborColors();
//	    }



    }


    public static void colorStates(Node[] maps){
        for(int i = 0; i<maps.length; i++){
            Node node = maps[i];
            System.out.println("///////////////");
            if(!node.getHasColor()){
                backtrackingSolution(node);
//                 localSearchSolution(node);
            }
        }


    }

    public static void backtrackingSolution(Node node){
        if(node != null){
            System.out.println("Node: "+ node.get_name());
            if(!node.getHasColor()) {
                Boolean set = node.setColor();
                if(set){
                    count++;
                }else{
                    System.out.println("Moving to previous node to change color");
                    node = node.getPrevious();
                    System.out.println("Now moving to " + node.get_name());
                    System.out.println();
                    node.changeColor();
                    backtrackingSolution(node);
                }
                //checks to see if there is a successful color set
            }
            Node neighbor = node.getUncoloredNeighbor();
            //Moves to neighbor
            if (neighbor != null) {
                System.out.println();
//                System.out.println("Neighbor: " + neighbor.get_name());
                node.setNext(neighbor);
                neighbor.setPrevious(node);
                backtrackingSolution(neighbor);
            //If there is no neighbor move back up the tree
            } else {
                System.out.println();
                node = node.getPrevious();
                if (node != null) {
                    node.checkNeighborColor2(node.getNeighbors(), node.getColor());
                }
                backtrackingSolution(node);
            }
        }else{
            System.out.println("Node is null");
        }
    }













    public static void localSearchSolution(Node[] map){
        setRandomMap(map);
        checkConflicts(map);
        bubbleSort(map);

//        LinkedList<Node> maps = new LinkedList<>(Arrays.asList(map));

        for(int i = 0; i<map.length; i++){
            localSearchUtil(map[i]);
        }

        checkConflicts(map);
        bubbleSort(map);
//        for(int i = 0; i<map.length; i++){
//            if(map[i].getConflicts() != 0) {
//                map[i].printNeighborColors();
//            }
//        }

        for(int i = 0; i<map.length; i++){
            System.out.println("Node: " + map[i].get_name() + " has color: " + map[i].getColor() + " has " + map[i].getConflicts() + " conflicts");
        }
    }

    public static void localSearchUtil(Node node){
        if(node.getConflicts() != 0) {
//            System.out.println("Working on " + node.get_name() + " it has " + node.getConflicts() + " conflicts!");
            Boolean work = node.setColorLocal();
            if(!work){
                localSearchNeighborUtil(node);

            }
        }
        node.setConflicts(0);
    }


    public static void localSearchNeighborUtil(Node node){

        List<Node> conflictNeighbors = node.getConflictingNeighbors();
        System.out.println("Conflicting Node: " + node.get_name() + " Color: " + node.getColor() );
        node.printNeighborColors();
        for(int i = 0; i<conflictNeighbors.size(); i++){
            Node neighbor = conflictNeighbors.get(i);
            System.out.println("Conflicting Neighbor: " + neighbor.get_name() + " color: " + neighbor.getColor());
            List<String> availableColors = neighbor.getAvailableColors();
            if(availableColors.size() != 0) {
                for (int j = 0; j < availableColors.size(); j++) {
                    if (neighbor.setAvailableColor(availableColors.get(j))) {
                        break;
                    }
                }
            }else{
//                System.out.println("Conflicting Node: " + neighbor.get_name() + " HAS NO AVALIABLE COLORS");
                List<Node> nonConflictingNeighbors = node.getNonConflictingNeighbors();
                for(int j = 0; j<nonConflictingNeighbors.size(); j++){
                    if(swapColors(node, nonConflictingNeighbors.get(j))){
                        break;
                    }
                }
            }
        }
//        node.setColorLocal();
//        System.out.println("New Node Color: "  + node.getColor());
        System.out.println("/////////////////////");

    }

    public static void setRandomMap(Node[] map){
        for(int i = 0; i<map.length; i++){
            Node node = map[i];
            node.setRandomColor();
        }
    }

    public static void checkConflicts(Node [] map){
        for(int i = 0; i<map.length; i++){
            map[i].checkConflicts();
        }
    }

    public static void checkConflicts(LinkedList<Node> map){
        for(int i = 0; i<map.size(); i++){
            map.get(i).checkConflicts();
        }
    }

    public static Node[] bubbleSort(Node [] map){
            int n = map.length;
            for (int i = 0; i < n-1; i++) {
                for (int j = 0; j < n - i-1; j++){
                    if (map[j].getConflicts() > map[j + 1].getConflicts()) {
                        // swap arr[j+1] and arr[i]
                        Node temp = map[j];
                        map[j] = map[j + 1];
                        map[j + 1] = temp;
                    }
                }
            }
            return map;
    }



    public static Node findState(Node[] states, String name){

        for(int i = 0; i<states.length; i++){
            if(states[i].get_name().equals(name)){
                return states[i];
            }
        }
        System.out.println("Returning a null node");
        return null;
    }

    public static int getCount(){
        return count;
    }

    public static Boolean swapColors(Node node1, Node node2){

        if(node1.checkNeighborColor(node2.getNeighbors(), node2.getColor()) && node2.checkNeighborColor(node1.getNeighbors(), node1.getColor())){
            System.out.println("Swapping " + node1.get_name() + " and " + node2.get_name());
            return true;
        }else{
            return false;
        }
    }
}


















