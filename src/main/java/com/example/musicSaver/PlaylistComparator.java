package com.example.musicSaver;

import java.util.Comparator;

import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

public class PlaylistComparator implements Comparator<Object> {

    @Override
    public int compare(Object p1, Object p2) {
        PlaylistSimplified playlist1 = (PlaylistSimplified) p1;
        PlaylistSimplified playlist2 = (PlaylistSimplified) p2;
        String name1 = playlist1.getName();
        String name2 = playlist2.getName();
        
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
