package com.example.musicSaver;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;
import java.net.URI;

import org.apache.hc.core5.http.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
RestController to handle all the Spotify authentification
**/

@RestController
@RequestMapping("/api")
public class AuthenticationController {

	private static final URI REDIRECT_URI = SpotifyHttpManager.makeUri("http://localhost:8080/api/user-code");

	private static final SpotifyApi SPOTIFY_API = new SpotifyApi.Builder()
										.setClientId(KeyReader.getInstance().getClientId())
										.setClientSecret(KeyReader.getInstance().getClientSecret())
										.setRedirectUri(REDIRECT_URI)
										.build();

	@GetMapping("/login")
	public String spotifyLogin() {
		AuthorizationCodeUriRequest request = SPOTIFY_API.authorizationCodeUri()
						.scope("user-read-private, user-read-email, user-top-read")
						.show_dialog(true)
						.build();
		final URI uri = request.execute();
		return uri.toString();
	}

	@GetMapping("/user-code")
	public String getSpotifyUserCode(@RequestParam Optional<String> code, HttpServletResponse response) throws IOException {
		String userCode = code.orElseThrow(() -> new IllegalArgumentException("Wrong code returned."));
		AuthorizationCodeRequest authCode = SPOTIFY_API.authorizationCode(userCode).build();
		
		try {
			final AuthorizationCodeCredentials credentials = authCode.execute();
			SPOTIFY_API.setAccessToken(credentials.getAccessToken());
			SPOTIFY_API.setRefreshToken(credentials.getRefreshToken());
			
			System.out.println("Expires in: " + credentials.getExpiresIn());
		} catch(IOException | ParseException | SpotifyWebApiException e) {
			e.printStackTrace();
		}
		
		response.sendRedirect("http://localhost:8080/playlists.html");
		return SPOTIFY_API.getAccessToken();
	}

	@GetMapping("/all-playlists")
	public Object[] getAllUserPlaylists() {
		List<PlaylistSimplified> allPlaylists = new ArrayList<PlaylistSimplified>();
		
		int offset = 0;
		do {
			Collections.addAll(allPlaylists, getUsersPlaylistsWithOffset(offset));
			offset += 20;
		} while(allPlaylists.size() == offset);
		
		String usersId = getUsersId();
		Object[] playlistOwnedByUser = allPlaylists.stream()
											.filter(playlist -> usersId.equals(playlist.getOwner().getId()))
											.toArray();
		return playlistOwnedByUser;
	}

	//maybe dont make it a REST
	//@GetMapping("/playlists")
	private PlaylistSimplified[] getUsersPlaylistsWithOffset(int offset) {
		GetListOfCurrentUsersPlaylistsRequest usersPlaylistsRequest = SPOTIFY_API.getListOfCurrentUsersPlaylists()
															.limit(20)
															.offset(offset)
															.build();

		Paging<PlaylistSimplified> usersPlaylists = null;
		try {
			usersPlaylists = usersPlaylistsRequest.execute();
		} catch(IOException | ParseException | SpotifyWebApiException e) {
			e.printStackTrace();
		}
		return usersPlaylists.getItems();
	}

	private String getUsersId() {
		GetCurrentUsersProfileRequest userRequest = SPOTIFY_API.getCurrentUsersProfile().build();

		User user = null;
		try {
			user = userRequest.execute();
		} catch(IOException | ParseException | SpotifyWebApiException e) {
			e.printStackTrace();
		}

		return user.getId();
	}

}