package com.company;

import java.util.*;

public class Node implements Comparable<Node> {

    private String name;
    private Boolean hasColor;
    private Node next;
    private Node previous;
    private LinkedList<Node> neighbors;
    private List<String> availableColors;
    private String color;
    private String possibleColors[] = new String[] {"Red", "Green", "Blue", "Yellow"};
    private int conflicts;
    private int steps;

    public Node(String name){
        this.name = name;
        this.neighbors = new LinkedList<Node>();
        this.hasColor = false;
        this.next = null;
        this.previous = null;
        this.availableColors = new ArrayList<String>(Arrays.asList(possibleColors));
        this.conflicts = 0;
        this.steps = 0;
    }

    public  String get_name() {
        return name;
    }
    public void set_name(String name) {
        this.name = name;
    }
    public LinkedList<Node> getNeighbors() {
        return neighbors;
    }
    public void addNeighbor(Node newNode) {
//		System.out.println("Adding Neighbor: " + newNode.get_name());
        this.neighbors.add(newNode);
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
//        System.out.println("SETTING " + get_name() + " to color: " + color);
        steps++;
        this.color = color;
    }
//    public String[] getPossibleColors() {
//        return possibleColors;
//    }
    public void setPossibleColors(String possibleColors[]) {
        this.possibleColors = possibleColors;
    }

    public Boolean getHasColor() {
        return hasColor;
    }

    public void setHasColor(Boolean hasColor) {
        this.hasColor = hasColor;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public int getConflicts() {
        return conflicts;
    }

    public void setConflicts(int conflicts) {
        this.conflicts = conflicts;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public void printNeighborsConflicts(){
        System.out.println(this.name + " has neighbors: ");
        for(int i =0; i< this.neighbors.size(); i++){
            System.out.println(this.neighbors.get(i).get_name() + " "+ this.neighbors.get(i).getConflicts());
        }
        System.out.println();
    }

    public void printNeighborColors(){
        System.out.println(this.name + " has neighbors: ");
        for(int i =0; i< this.neighbors.size(); i++){
            System.out.println(this.neighbors.get(i) + " color: "+ this.neighbors.get(i).getColor());
        }
//        System.out.println();
    }


    public void setRandomColor(){
        Random r = new Random();
        int index = r.nextInt(this.possibleColors.length);
//        System.out.println("Setting Randomly "+ this.get_name() + " to color: " + this.possibleColors[index]);
        this.setColor(this.possibleColors[index]);
        this.hasColor = true;
        this.availableColors.remove(this.possibleColors[index]);

    }


    public Boolean setColor(){
        for(int i = 0; i<availableColors.size(); i++){
//            System.out.println("Trying color: " + availableColors.get(i));
            String color = availableColors.get(i);
            if(this.previous == null && this.next == null){
//                System.out.println("Setting "+ this.get_name() + " to color: " + color);
                this.setColor(color);
                this.hasColor = true;
                return true;
            }else if(this.previous != null && this.next == null){
                if(!color.equals(this.previous.getColor()) && checkNeighborColor(neighbors, color)){
//                    System.out.println("Setting "+ this.get_name() + " to color: " + color);
                    this.setColor(color);
                    this.hasColor = true;
                    return true;
                }
            }else if(this.previous != null && this.next != null){
                if(!color.equals(this.previous.getColor()) && !color.equals(this.next.getColor()) && checkNeighborColor(neighbors, color)){
//                    System.out.println("Setting "+ this.get_name() + " to color: " + color);
                    this.setColor(color);
                    this.hasColor = true;
                    return true;
                }
            }else{
//                System.out.println("Removing color: " + color);
                this.availableColors.remove(color);
            }
        }
//        System.out.println("NO COLOR IS AVAILABLE");
        return false;
    }







    public Boolean setColorLocal(){
        sortNeighbors();
        int count = 100;
        int index = 0;
        int neighborSize = 0;
        for(int i = 0; i<possibleColors.length; i++){
            String color = possibleColors[i];
            Node neighbor = checkNodeNeighborColor( neighbors, color);
            if(neighbor == null){
                this.setColor(color);
                this.hasColor = true;
//                System.out.println("Setting " + this.get_name() + " to color: " + color);
                return true;
            }else{
                if(neighbor.neighbors.size() < count){
                    index = i;
                    count = neighbor.neighbors.size();
                }
            }
        }
        String color = possibleColors[index];
//        System.out.println("Setting " + this.get_name() + " to color: " + color);
        this.setColor(color);
        return false;
    }



    @Override
    public int compareTo(Node object) {
        return this.getConflicts()-object.getConflicts();
    }

    public Node checkNodeNeighborColor(LinkedList<Node> list, String color){
        for(int i = 0; i<list.size(); i++){
            if(list.get(i).getColor() != null){
                if(list.get(i).getColor().equals(color)){
                    return list.get(i);
                }
            }
        }
        return null;
    }


    public void sortNeighbors(){
        int n = this.neighbors.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++){
                Node node1 = this.neighbors.get(j);
                Node node2 = this.neighbors.get(j+1);

                if (node1.neighbors.size() > node2.neighbors.size()) {

                    // swap arr[j+1] and arr[i]
                    Node temp = this.neighbors.get(j);
                    this.neighbors.set(j, this.neighbors.get(j+1)) ;
                    this.neighbors.set(j+1, temp) ;
                }
            }
        }
    }


    public void checkConflicts(){
        for(int i = 0; i<neighbors.size(); i++){
            if(this.color.equals(neighbors.get(i).getColor())){
                this.conflicts++;
            }
        }
    }

    public Boolean checkNeighborColor(LinkedList<Node> list, String color){
        for(int i = 0; i<list.size(); i++){
            if(list.get(i).getColor() != null){
                if(list.get(i).getColor().equals(color)){
//                    System.out.println("Return false");
                    return false;
                }
            }
        }
        return true;
    }



    public Boolean changeColor() {
        System.out.println("Removing color: " + this.color);
        this.availableColors.remove(this.color);
        if(this.availableColors.size() != 0){
           return this.setColor();

        }else{
            return false;
        }
    }





    public void checkNeighborColor2(LinkedList<Node> list, String color){
        for(int i = 0; i<list.size(); i++){
            if(list.get(i).getColor() != null &&  list.get(i).getColor().equals(color)){
                System.out.println("FOUND AN ERROR! " + list.get(i).get_name() + " and " + this.get_name() + " have the same color: " + color);
            }
        }
    }


    public Node getUncoloredNeighbor(){
        for(int i = 0; i<neighbors.size(); i++){
            if(!neighbors.get(i).hasColor){
//                System.out.println("Node " + neighbors.get(i).get_name() + " has color: " + neighbors.get(i).hasColor + ", Moving to " + neighbors.get(i).get_name());

                return neighbors.get(i);
            }
        }
//        System.out.println("RETURNING NULL, NO NON-COLORED NEIGHBORS!");
        return null;
    }




    public Boolean checkConflicts(String color){
        for(int i = 0; i<neighbors.size(); i++){
            if(color.equals(neighbors.get(i).getColor())){
                return false;
            }
        }
        return true;
    }


    public LinkedList<Node> getConflictingNeighbors(){
        LinkedList<Node> conflicts = new LinkedList<>();
        for(int i = 0; i<neighbors.size(); i++){
            Node neighbor = neighbors.get(i);
            if(this.color.equals(neighbor.getColor())){
                conflicts.add(neighbor);
            }
        }
        return conflicts;
    }

    public List<Node> getNonConflictingNeighbors(){
        List<Node> nonConflicts = new ArrayList<>();
        for(int i = 0; i<neighbors.size(); i++){
            Node neighbor = neighbors.get(i);
            if(!this.color.equals(neighbor.getColor())){
                nonConflicts.add(neighbor);
            }
        }
        return nonConflicts;
    }


    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
