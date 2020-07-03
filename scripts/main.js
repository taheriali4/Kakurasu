/*!
 *
 *  Web Starter Kit
 *  Copyright 2015 Google Inc. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 *
 */
/* eslint-env browser */
(function() {
  'use strict';

  // Check to make sure service workers are supported in the current browser,
  // and that the current page is accessed from a secure origin. Using a
  // service worker from an insecure origin will trigger JS console errors. See
  // http://www.chromium.org/Home/chromium-security/prefer-secure-origins-for-powerful-new-features
  var isLocalhost = Boolean(window.location.hostname === 'localhost' ||
      // [::1] is the IPv6 localhost address.
      window.location.hostname === '[::1]' ||
      // 127.0.0.1/8 is considered localhost for IPv4.
      window.location.hostname.match(
        /^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/
      )
    );

  if ('serviceWorker' in navigator &&
      (window.location.protocol === 'https:' || isLocalhost)) {
    navigator.serviceWorker.register('service-worker.js')
    .then(function(registration) {
      // updatefound is fired if service-worker.js changes.
      registration.onupdatefound = function() {
        // updatefound is also fired the very first time the SW is installed,
        // and there's no need to prompt for a reload at that point.
        // So check here to see if the page is already controlled,
        // i.e. whether there's an existing service worker.
        if (navigator.serviceWorker.controller) {
          // The updatefound event implies that registration.installing is set:
          // https://slightlyoff.github.io/ServiceWorker/spec/service_worker/index.html#service-worker-container-updatefound-event
          var installingWorker = registration.installing;

          installingWorker.onstatechange = function() {
            switch (installingWorker.state) {
              case 'installed':
                // At this point, the old content will have been purged and the
                // fresh content will have been added to the cache.
                // It's the perfect time to display a "New content is
                // available; please refresh." message in the page's interface.
                break;

              case 'redundant':
                throw new Error('The installing ' +
                                'service worker became redundant.');

              default:
                // Ignore
            }
          };
        }
      };
    }).catch(function(e) {
      console.error('Error during service worker registration:', e);
    });
  }

  // Your custom JavaScript goes here

  


  
})();

function makePuzzle(){
    resize(document.getElementById("size").value);

}

  function resize(value){
    document.getElementById("test").innerHTML = "size = " + value;
    getPuzzle(value);
  }


  function getPuzzle(size){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        //return this.responseText;
        buildPuzzle(this.responseText, size);
        console.log(this.responseText)
    }};
    xhttp.open("GET", "puzzle?" + size, true);
    xhttp.send();
  }

        

function buildPuzzle(string, size) {
        console.log("puzzle build " + size);
    var arr = string.split(" ");
    console.log("size: " + size + " rows,cols: " +  arr)
    var body = document.getElementsByTagName('body')[0];
    var tbl = document.createElement('table');
    tbl.style.width = '100%';
    tbl.setAttribute('border', '0');
    var tbdy = document.createElement('tbody');
    //generate the table
    for (var i = 0; i <= size; i++) { //rows
      var tr = document.createElement('tr');
      for (var j = 0; j <= size; j++) { //columns
        var td = document.createElement('td');
          if(i == 0){
            td.innerHTML = j
          }else if(j == 0){
            td.innerHTML = i
          }
          if(i != 0 && j != 0){
            var btn = document.createElement('BUTTON');
            btn.style.height= "50px";
            btn.style.width= "100%";
            btn.style.border= "1";
            btn.style.backgroundColor="#FFFFFF";
            //td.setAttribute('onclick', 'mark()');
            btn.onclick = function(){mark(this);}
            td.appendChild(btn);
          }
          td.appendChild(document.createTextNode('\u0020'))
          tr.appendChild(td)
        }
        if(i != 0){
            var td = document.createElement('td');
            var index = parseInt(size) + i - 1;
            td.innerHTML = arr[index];
            tr.appendChild(td);
        }
        tbdy.appendChild(tr);
    }
    //this creates the 'answer bits' on the bottom
    tr = document.createElement('tr');
    tr.appendChild(document.createElement('td'));
    for(var i = 0; i < size; i ++){
        var td = document.createElement('td');
        td.innerHTML = arr[i];
        tr.appendChild(td);
    }
    tbdy.appendChild(tr);

    tbl.appendChild(tbdy);
    document.getElementById("puzzle").innerHTML = '';
    document.getElementById("puzzle").appendChild(tbl);
    //body.appendChild(tbl)
}

function mark(btn){
    if(btn.className != 'toggled'){
      btn.style.backgroundColor = "#000000";
      btn.className = 'toggled';
    }else{
      btn.className = 'nt';
      btn.style.backgroundColor = "#FFFFFF";
    }
}
