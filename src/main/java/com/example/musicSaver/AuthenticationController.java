package com.example.musicSaver;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;

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
										.setClientId(Keys.getInstance().getClientId())
										.setClientSecret(Keys.getInstance().getClientSecret())
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
		String userCode = code.orElseGet(() -> "not provided");	//change this
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

	@GetMapping("/playlists")
	public PlaylistSimplified[] getUserPlaylists() {	// make async
		GetListOfCurrentUsersPlaylistsRequest usersPlaylistsRequest = SPOTIFY_API.getListOfCurrentUsersPlaylists()
															.limit(50)
															.build();

		Paging<PlaylistSimplified> usersPlaylists = null;
		try {
			usersPlaylists = usersPlaylistsRequest.execute();
		} catch(IOException | ParseException | SpotifyWebApiException e) {
			e.printStackTrace();
		}
		PlaylistSimplified[] playlists = usersPlaylists.getItems();
		return usersPlaylists.getItems();
	}
}
