<%@ page import="com.google.sps.map.Map" %>
<% String apiKey = System.getenv(Map.API_KEY_ENVIRONMENT_VARIABLE); %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Stefano's Portfolio</title>
    <link rel="stylesheet" href="style.css">
    <script src="script.js"></script>
    <script
      src="https://maps.googleapis.com/maps/api/js?key=<%= apiKey %>&callback=mapInit"
      defer
    ></script>
  </head>
  <body onload="commentsInit()">
    <div id="content">
      <h1>Stefano's Portfolio</h1>
      <img src="/images/selfie.jpg" alt="Stefano's selfie" width="250">
      <p>I am Stefano and I am from Italy.</p>
      <div id="education" class="section collapsed">
          <div class="title" onclick="toggleDescription('education')">
              <h2>Education</h2>
          </div>
          <div class="description">
              <p>I am pursuing a bachelor's degree in Computer Science and Engineering at the Politecnico di Torino and I am in the second year.</p>
          </div>
      </div>
      <div id="projects" class="section collapsed">
          <div class="title" onclick="toggleDescription('projects')">
              <h2>Projects</h2>
          </div>
          <div class="description">
              <h3>DRAFT</h3>
              <p>The DRones Autonomous Flight Team is a student team whose aim is to build an autonomous drone.<br>
              I am in the computer vision squad.</p>
              <h3>Zero Robotics</h3>
              <p><a href="http://zerorobotics.mit.edu/">Zero Robotics</a> is an international competition organized by MIT, NASA and ESA in which you have to program a miniaturized satellite abroad the International Space Station.</p>
          </div>
      </div>
      <div id="hobbies" class="section collapsed">
        <div class="title" onclick="toggleDescription('hobbies')">
            <h2>Hobbies & Passions</h2>
        </div>
        <div class="description">
            <button class="random" onclick="randomSlide(this)">Get a random slide</button>
            <div class="slider">
                <div class="slide">
                    <img src="/images/tennis.jpg" alt="Tennis" class="slide-image">
                    <div class="slide-text">I play tennis.</div>
                </div>
                <div class="slide hidden">
                    <img src="/images/AI.jpg" alt="Artificial Intelligence" class="slide-image">
                    <div class="slide-text">
                        I am fascinated by artificial intelligence and I would like to learn more about it.
                    </div>
                </div>
                <div class="slide hidden">
                    <img src="/images/walk.jpg" alt="Walking" class="slide-image">
                    <div class="slide-text">
                        I like to walk or ride my bike in the fresh air.
                    </div>
                </div>
                <div class="slide hidden">
                    <img src="/images/soccer.jpg" alt="Soccer" class="slide-image">
                    <div class="slide-text">I am a soccer fan.</div>
                </div>
                <div class="slide hidden">
                    <img src="/images/tv.jpg" alt="TV Shows" class="slide-image">
                    <div class="slide-text">I like watching TV Shows.</div>
                </div>
                <button class="change prev" onclick="previousSlide(this)">❮</button>
                <button class="change next" onclick="nextSlide(this)">❯</button>
            </div>
        </div>
      </div>
      <div id="map" class="section collapsed">
        <div class="title" onclick="toggleDescription('map')">
            <h2>Map</h2>
        </div>
        <div class="description">
            <div id="map-container"></div>
        </div>
      </div>
      <div id="links" class="section collapsed">
          <div class="title" onclick="toggleDescription('links')">
              <h2>Links</h2>
          </div>
          <div class="description">
              <h3><a href="https://it.linkedin.com/in/stefano-gioda-797430158">LinkedIn</a></h3>
              <h3><a href="https://github.com/Giost">GitHub</a></h3>
          </div>
      </div>
      <div id="comments" class="section collapsed">
          <div class="title" onclick="toggleDescription('comments')">
              <h2>Comments</h2>
          </div>
          <div class="description">
              <div class="comment-translation">
                <label for="comment-language">Language of the comments </label>
                <select name="comment-language" id="comment-language" onchange="fetchComments()">
                  <option value="EN">English</option>
                  <option value="AF">Afrikaans</option>
                  <option value="SQ">Albanian</option>
                  <option value="AR">Arabic</option>
                  <option value="HY">Armenian</option>
                  <option value="EU">Basque</option>
                  <option value="BN">Bengali</option>
                  <option value="BG">Bulgarian</option>
                  <option value="CA">Catalan</option>
                  <option value="KM">Cambodian</option>
                  <option value="ZH">Chinese (Mandarin)</option>
                  <option value="HR">Croatian</option>
                  <option value="CS">Czech</option>
                  <option value="DA">Danish</option>
                  <option value="NL">Dutch</option>
                  <option value="ET">Estonian</option>
                  <option value="FJ">Fiji</option>
                  <option value="FI">Finnish</option>
                  <option value="FR">French</option>
                  <option value="KA">Georgian</option>
                  <option value="DE">German</option>
                  <option value="EL">Greek</option>
                  <option value="GU">Gujarati</option>
                  <option value="HE">Hebrew</option>
                  <option value="HI">Hindi</option>
                  <option value="HU">Hungarian</option>
                  <option value="IS">Icelandic</option>
                  <option value="ID">Indonesian</option>
                  <option value="GA">Irish</option>
                  <option value="IT">Italian</option>
                  <option value="JA">Japanese</option>
                  <option value="KO">Korean</option>
                  <option value="LA">Latin</option>
                  <option value="LV">Latvian</option>
                  <option value="LT">Lithuanian</option>
                  <option value="MK">Macedonian</option>
                  <option value="MS">Malay</option>
                  <option value="ML">Malayalam</option>
                  <option value="MT">Maltese</option>
                  <option value="MI">Maori</option>
                  <option value="MR">Marathi</option>
                  <option value="MN">Mongolian</option>
                  <option value="NE">Nepali</option>
                  <option value="NO">Norwegian</option>
                  <option value="FA">Persian</option>
                  <option value="PL">Polish</option>
                  <option value="PT">Portuguese</option>
                  <option value="RO">Romanian</option>
                  <option value="RU">Russian</option>
                  <option value="SM">Samoan</option>
                  <option value="SR">Serbian</option>
                  <option value="SK">Slovak</option>
                  <option value="SL">Slovenian</option>
                  <option value="ES">Spanish</option>
                  <option value="SW">Swahili</option>
                  <option value="SV">Swedish </option>
                  <option value="TA">Tamil</option>
                  <option value="TT">Tatar</option>
                  <option value="TE">Telugu</option>
                  <option value="TH">Thai</option>
                  <option value="BO">Tibetan</option>
                  <option value="TO">Tonga</option>
                  <option value="TR">Turkish</option>
                  <option value="UK">Ukrainian</option>
                  <option value="UR">Urdu</option>
                  <option value="UZ">Uzbek</option>
                  <option value="VI">Vietnamese</option>
                  <option value="CY">Welsh</option>
                </select>  
              </div>
              <div class="comment-pagination">
                <label for="comment-limit">Comments per page </label>
                <select name="comment-limit" id="comment-limit" onchange="changeLimit()">
                  <option>5</option>
                  <option>10</option>
                  <option>15</option>
                  <option>20</option>
                </select>
                <button id="first-page" onclick="setPage(1)">First</button>
                <button id="prev-page" onclick="changePage(-1)">Previous</button>
                <span id="current-page">1</span> / <span id="total-pages"></span>
                <button id="next-page" onclick="changePage(1)">Next</button>
                <button id="last-page" onclick="setLastPage()">Last</button>
                <label for="to-page">Go to page </label>
                <input type="number" id="to-page" name="to-page">
                <button onclick="goToPage()">Go</button>
              </div>
              <div id="comments-container"></div>
              <div class="comment-form-container">
                <form id="comment-form" class="hidden" action="/comments/data" method="POST">
                  <p>Enter a comment:</p>
                  <textarea name="comment-content" required></textarea>
                  <br/><br/>
                  <input type="submit" />
                </form>
                <a id="login-link">Login to comment</a>
              </div>
          </div>
      </div>
    </div>
  </body>
</html>
