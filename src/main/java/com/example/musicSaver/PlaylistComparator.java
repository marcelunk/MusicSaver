package com.example.musicSaver;

import java.util.Comparator;

import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

public class PlaylistComparator implements Comparator<PlaylistSimplified> {

    @Override
    public int compare(PlaylistSimplified p1, PlaylistSimplified p2) {
        String name1 = p1.getName().toLowerCase();
        String name2 = p2.getName().toLowerCase();
        
        if(name1 == name2) {
            return 0;
        } if(name1 == null) {
            return -1;
        } if(name2 == null) {
            return 1;
        }
        return name1.compareTo(name2);
    }
    
}
