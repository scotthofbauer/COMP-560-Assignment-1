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
    private static int backtrackSteps = 0;


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

                //Create a 
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

        System.out.println("Backtracking Solution" );
        colorStates(map);
        printMap(map);
        System.out.println("Backtracking Solution Searched " + backtrackSteps + " States to complete");
        System.out.println();
        System.out.println("///////////////");
        System.out.println();
        System.out.println("Local Search Solution");
        localSearchSolution(map);
        printMap(map);
        System.out.println();
        System.out.println("Local Search Solution had " + sumLocalSolutionSteps(map) + " changes to proposed solution");



    }



    public static void colorStates(Node[] maps){
        for(int i = 0; i<maps.length; i++){
            Node node = maps[i];
//            System.out.println("///////////////");
            if(!node.getHasColor()){
                backtrackingSolution(node);
            }
        }


    }

    public static void backtrackingSolution(Node node){
        if(node != null){
            backtrackSteps++;
            if(!node.getHasColor()) {
                Boolean set = node.setColor();
                if(set){
                    count++;
                }else{
                    node = node.getPrevious();
                    node.changeColor();
                    backtrackingSolution(node);
                }
            }
            Node neighbor = node.getUncoloredNeighbor();

            if (neighbor != null) {
                node.setNext(neighbor);
                neighbor.setPrevious(node);
                backtrackingSolution(neighbor);
            } else {
                node = node.getPrevious();
                if (node != null) {
                    node.checkNeighborColor2(node.getNeighbors(), node.getColor());
                }
                backtrackingSolution(node);
            }
        }
    }





    public static void localSearchSolution(Node[] map){
        setRandomMap(map);
        checkConflicts(map);
        int count = 0;
        String previous = "XX";

        while(!checkSolution(map) && count <= 100){

            Arrays.sort(map);
            if(!previous.equals(map[47].get_name())) {
//                System.out.println("coloring " + map[0].get_name() + " with " + map[0].getConflicts());
                map[47].setColorLocal();
                map[47].setConflicts(0);
                map[47].checkConflicts();
                previous = map[47].get_name();
//                System.out.println("Node: "+ map[0].get_name() + " has " + map[0].getConflicts() + " conflicts");

            }else{
//                System.out.println("coloring " + map[1].get_name() + " with " + map[1].getConflicts());
                map[46].setColorLocal();
                map[46].setConflicts(0);
                map[46].checkConflicts();
                previous = map[46].get_name();
//                System.out.println("Node: "+ map[1].get_name() + " has " + map[1].getConflicts() + " conflicts");

            }

//            System.out.println(count);
            checkConflicts(map);

            count++;
        }

        if(!checkSolution(map)){
//            System.out.println("RESETING MAP");
            setRandomMap(map);
            localSearchSolution(map);
        }else{

//            System.out.println("HOLY FUCK IT WORKED");
        }

//        checkConflicts(map);

    }






    public static Boolean checkSolution(Node[] map){
        for(int i = 0; i<map.length; i++){
            if(map[i].getConflicts() != 0) {
                return false;
            }
        }
        return true;
    }


    public static void setRandomMap(Node[] map){
        for(int i = 0; i<map.length; i++){
            Node node = map[i];
            node.setRandomColor();
        }
    }

    public static void resetConflicts(Node [] map){
        for(int i = 0; i<map.length; i++){
            map[i].setConflicts(0);
        }
    }

    public static void checkConflicts(Node [] map){
        resetConflicts(map);
        for(int i = 0; i<map.length; i++){
            map[i].checkConflicts();
        }
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

//    public static void printMap(Node[] map) {
//        for (int i = 0; i < map.length; i++) {
//            System.out.println(map[i].get_name() + " : " + map[i].getColor() + " has " + map[i].getConflicts() + " conflicts");
//
//        }
//    }

    public static void printMap(Node[] map) {
        for (int i = 0; i < map.length; i++) {
            System.out.println(map[i].get_name() + " : " + map[i].getColor());

        }
    }

    public static int sumLocalSolutionSteps(Node[] map) {
        int count = 0;
        for (int i = 0; i < map.length; i++) {
//            System.out.println("Node: " + map[i].get_name() + " has color: " + map[i].getColor() + " has " + map[i].getConflicts() + " conflicts");
            count += map[i].getSteps();
        }
        return count;
    }

}


















