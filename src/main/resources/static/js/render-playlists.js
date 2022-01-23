const section = document.querySelector('section');
const playlistsToDownload = new Set();

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
    var row = document.createElement('div');
    row.classList.add('playlist');
    row.id = data[i].id;
    //row.onclick = downloadPlaylist;
    row.onclick = addToDownload;

    var name = document.createElement('p');
    name.classList.add('name');
    name.textContent = data[i].name;

    var cover = document.createElement('img');
    cover.classList.add('cover');
    var images = data[i].images
    if (images.length == 3) {
      cover.src = images[2].url;
    } else {
      cover.src = "http://localhost:8080/images/default.png";
    }

    row.appendChild(cover);
    row.appendChild(name);
    playlists.appendChild(row);
  }

  section.appendChild(playlists);
}

function downloadPlaylist() {
  fetch("http://localhost:8080/api/selected-playlist?playlist_id=" + this.id)
  .then(response => response.json())
  .then(data => {
    if(data) {
      const downloadTag = document.createElement('a');     
      downloadTag.setAttribute("href", "http://localhost:8080/playlists/myPlaylist.txt");
      downloadTag.setAttribute("download", "myPlaylist");
      document.body.appendChild(downloadTag);
      downloadTag.click();
      document.body.removeChild(downloadTag);
    } else {
      alert("Ups, try again!")
    }
  })
}

function downloadPlaylists() {
  var playlist_ids = Array.from(playlistsToDownload);
  fetch("http://localhost:8080/api/selected-playlists?playlist_ids=" + playlist_ids)
  .then(response => response.json())
  .then(data => {
    if(data) {
      alert("Worked!")
    } else {
      alert("Ups, try again!")
    }
  })
}

function addToDownload() {
  playlistToDownload = playlistsToDownload.add(this.id);
}

function printPlaylists() {
  for (let item of playlistsToDownload) console.log(item);
}