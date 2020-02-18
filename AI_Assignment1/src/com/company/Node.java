package com.company;

import java.util.*;

public class Node {

    private String name;
    private Boolean hasColor;
    private Node next;
    private Node previous;
    private LinkedList<Node> neighbors;
    private List<String> availableColors;
    private String color;
    private String possibleColors[] = new String[] {"Red", "Green", "Blue", "Yellow"};
    private int conflicts;

    public Node(String name){
        this.name = name;
        this.neighbors = new LinkedList<Node>();
        this.hasColor = false;
        this.next = null;
        this.previous = null;
        this.availableColors = new ArrayList<String>(Arrays.asList(possibleColors));
        this.conflicts = 0;
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

    public void printNeighbors(){
        System.out.println(this.name + " has neighbors: ");
        for(int i =0; i< this.neighbors.size(); i++){
            System.out.println(this.neighbors.get(i));
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

    public Boolean setAvailableColor(String color){
        if(checkConflicts(color)){
            System.out.println("Resetting " + this.get_name() + " color from: " + this.color + " to -> " + color);
            this.setColor(color);
            return true;
        }else{
            return false;
        }


    }

    public Boolean setColor(){
        for(int i = 0; i<availableColors.size(); i++){
            System.out.println("Trying color: " + availableColors.get(i));
            String color = availableColors.get(i);
            if(this.previous == null && this.next == null){
                System.out.println("Setting "+ this.get_name() + " to color: " + color);
                this.setColor(color);
                this.hasColor = true;
                return true;
            }else if(this.previous != null && this.next == null){
                if(!color.equals(this.previous.getColor()) && checkNeighborColor(neighbors, color)){
                    System.out.println("Setting "+ this.get_name() + " to color: " + color);
                    this.setColor(color);
                    this.hasColor = true;
                    return true;
                }
            }else if(this.previous != null && this.next != null){
                if(!color.equals(this.previous.getColor()) && !color.equals(this.next.getColor()) && checkNeighborColor(neighbors, color)){
                    System.out.println("Setting "+ this.get_name() + " to color: " + color);
                    this.setColor(color);
                    this.hasColor = true;
                    return true;
                }
            }else{
                System.out.println("Removing color: " + color);
                this.availableColors.remove(color);
            }
        }
        System.out.println("NO COLOR IS AVAILABLE");
        return false;
    }













    public Boolean setColorLocal(){
//        System.out.println("Avaliable Color size: " + availableColors.size());

        for(int i = 0; i<possibleColors.length; i++){
//            System.out.println("Trying color: " + availableColors.get(i));
            String color = possibleColors[i];
            if(checkNeighborColor(neighbors, color)){
//                    System.out.println("Setting "+ this.get_name() + " to color: " + color);
//                    System.out.println();
                    this.setColor(color);
                    this.hasColor = true;
                    return true;
            }
        }
//        System.out.println("NO COLOR IS AVAILABLE");
//        System.out.println();
        return false;
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



    public Boolean checkNeighborColor(LinkedList<Node> list, String color){
        for(int i = 0; i<list.size(); i++){
            if(list.get(i).getColor() != null){
                if(list.get(i).getColor().equals(color)){
                    return false;
                }
            }
        }
        return true;
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
                System.out.println("Node " + neighbors.get(i).get_name() + " has color: " + neighbors.get(i).hasColor + ", Moving to " + neighbors.get(i).get_name());

                return neighbors.get(i);
            }
        }
        System.out.println("RETURNING NULL, NO NON-COLORED NEIGHBORS!");
        return null;
    }


    public Node getAnyNeighbor(){
        for(int i = 0; i<neighbors.size(); i++){
            if(!neighbors.get(i).hasColor){
                System.out.println("Node " + neighbors.get(i).get_name() + " has color: " + neighbors.get(i).hasColor + ", Moving to " + neighbors.get(i).get_name());

                return neighbors.get(i);
            }
        }
        System.out.println("RETURNING NULL, NO NON-COLORED NEIGHBORS!");
        return null;
    }

    public void checkConflicts(){
        for(int i = 0; i<neighbors.size(); i++){
            if(this.color.equals(neighbors.get(i).getColor())){
                this.conflicts++;
            }
        }
    }

    public Boolean checkConflicts(String color){
        for(int i = 0; i<neighbors.size(); i++){
            if(color.equals(neighbors.get(i).getColor())){
                return false;
            }
        }
        return true;
    }

    public List<String> getAvailableColors(){
        availableColors.remove(this.getColor());
        for(int i = 0; i<neighbors.size(); i++){
            if(neighbors.get(i) != null) {
                String neighborColor = neighbors.get(i).getColor();
                if (availableColors.contains(neighborColor)) {
                    availableColors.remove(neighborColor);
                }
            }
        }
       return availableColors;
    }

    public List<Node> getConflictingNeighbors(){
        List<Node> conflicts = new ArrayList<>();
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





}
