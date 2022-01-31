package com.example.musicSaver;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;

public class PlaylistWriterUtil {

	public static String writePlaylistsToZip(Map<String, String> playlists) {
		UUID archiveId = UUID.randomUUID();

		try (ZipOutputStream zipOut = 
			new ZipOutputStream(new FileOutputStream(".\\src\\main\\resources\\static\\playlists\\"+ archiveId.toString() +".zip"))) {
			
			Set<String> playlistNames = playlists.keySet();
			for(String name : playlistNames) {
				zipOut.putNextEntry(new ZipEntry(name + ".txt"));
				String tracks = playlists.get(name);
				byte[] tracksData = tracks.getBytes();
				zipOut.write(tracksData, 0, tracksData.length);
				zipOut.closeEntry();
			}
			zipOut.flush();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return archiveId.toString();
	}

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

    public static String tracksToString(PlaylistTrack[] tracks) {
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<tracks.length; i++) {
			Object object = tracks[i].getTrack();
			Track track = null;
			if(object instanceof Track) {
				track = (Track) object;
			} else {
				throw new IllegalArgumentException("Object should be of type Track but was " + object.getClass());
			}
			builder.append(track.getName() + " - ");
			
			ArtistSimplified[] artists = track.getArtists();
			for(int j=0; j<artists.length; j++) {
				builder.append(artists[j].getName() + " ");
			}
			builder.append(System.getProperty("line.separator"));

		}
		return builder.toString().trim();		
	}
}
