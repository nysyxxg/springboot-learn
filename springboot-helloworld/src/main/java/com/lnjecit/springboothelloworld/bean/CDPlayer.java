package com.lnjecit.springboothelloworld.bean;

public class CDPlayer {
    private CompactDisc compactDisc;

    public CDPlayer(CompactDisc compactDisc) {
        this.compactDisc = compactDisc;
    }

    public void play() {
        System.out.println("------CDPlayer-------play---");
        compactDisc.play();
    }
}