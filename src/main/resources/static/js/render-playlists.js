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
    var row = document.createElement('div');

    var name = document.createElement('p');
    name.textContent = data[i].name;

    var cover = document.createElement('img');
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