const section = document.querySelector('section');

function usersPlaylists() {
    fetch("http://localhost:8080/api/all-playlists")
    .then(response => response.json())
    .then(data => {
      showPlaylists(data);
      console.log(data);
    })
}

function showPlaylists(data) {
  var playlists = document.createElement('article');

  for(var i=0; i<data.length; i++) {
    var playlist = document.createElement('p');
    playlist.textContent = data[i].name;
    playlists.appendChild(playlist);
  }

  section.appendChild(playlists);
}