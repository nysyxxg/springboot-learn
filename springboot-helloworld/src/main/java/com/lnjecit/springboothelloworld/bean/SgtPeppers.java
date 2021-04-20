package com.lnjecit.springboothelloworld.bean;

public class SgtPeppers implements CompactDisc {

    @Override
    public void play() {
        System.out.println("------SgtPeppers-------play---");
        String title = "Sgt.Pepper's Lonely Hearts Club Band";
        String artists = "The Beatles";
        System.out.println("------Playing " + title + " By " + artists);
    }
}