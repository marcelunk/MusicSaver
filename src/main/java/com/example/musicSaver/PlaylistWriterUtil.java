package com.example.musicSaver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;

public class PlaylistWriterUtil {

    public static boolean writePlaylistToFile(String playlistName, PlaylistTrack[] tracks) {
		String output = tracksToString(tracks);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(".\\src\\main\\resources\\static\\playlists\\" + playlistName + ".txt"))) {
			writer.write(output);			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
    
    private static String tracksToString(PlaylistTrack[] tracks) {
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<tracks.length; i++) {
			Object object = tracks[i].getTrack();
			Track track = null;
			if(object instanceof Track) {
				track = (Track) object;
			} else {
				throw new IllegalArgumentException("Object should be of type Track but was " + object.getClass());
			}
			builder.append(track.getName() + "- ");
			
			ArtistSimplified[] artists = track.getArtists();
			for(int j=0; j<artists.length; j++) {
				builder.append(artists[j].getName() + " ");
			}
			builder.append(System.getProperty("line.separator"));

		}
		return builder.toString().trim();		
	}
}
