// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

const DEFAULT_COMMENT_LIMIT = 5;

/**
 * Checks the validity of the parameters.
 */
function check(element, classAttribute) {
  if (!element || !element.classList) {
    throw new Error("The element argument is not valid.");
  }
  if (!classAttribute || typeof classAttribute !== "string") {
    throw new Error("The classAttribute argument is not a string.");
  }
}

/**
 * Checks if the element has the class.
 */
function hasClass(element, classAttribute) {
  check(element, classAttribute);
  return element.classList.contains(classAttribute);
}

/**
 * Adds the class in the element.
 */
function addClass(element, classAttribute) {
  check(element, classAttribute);
  element.classList.add(classAttribute);
}

/**
 * Remove the class in the element.
 */
function removeClass(element, classAttribute) {
  check(element, classAttribute);
  element.classList.remove(classAttribute);
}

/**
 * Inserts/removes the class in the element.
 */
function toggleClass(element, classAttribute) {
  if (hasClass(element, classAttribute)) {
    removeClass(element, classAttribute);
  } else {
    addClass(element, classAttribute);
  }
}

/**
 * Shows/hides the description inside the section.
 */
function toggleDescription(sectionId) {
  const sectionElement = document.getElementById(sectionId);
  if (!sectionElement) {
    throw new Error("toggleDescription wrong sectionId.");
  }
  toggleClass(sectionElement, "collapsed");
}

/**
 * Hides the currently displayed slide and return the total number of slides
 * and the index of the currently displayed.
 */
function hideAllSlides(slider) {
  let totalSlides = 0;
  let currentlyDisplayed;
  for (const elem of slider.children) {
    if (hasClass(elem, "slide")) {
      if (!hasClass(elem, "hidden")) {
        currentlyDisplayed = totalSlides;
        addClass(elem, "hidden");
      }
      totalSlides++;
    }
  }
  return {totalSlides, currentlyDisplayed};
}

/**
 * Changes the slide to the current one + offset.
 */
function changeSlide(offset, slider) {
  if (!offset || !Number.isInteger(offset)) {
    throw new Error("changeSlide first argument is not an integer.");
  }
  const {totalSlides, currentlyDisplayed} = hideAllSlides(slider);
  const slideToShow = slider.children[(currentlyDisplayed + offset + totalSlides) % totalSlides];
  removeClass(slideToShow, "hidden");
}

/**
 * Changes the slide to the previous one.
 */
function previousSlide(previousButton) {
  // The parentElement of the button is the slider container
  changeSlide(-1, previousButton.parentElement);
}

/**
 * Changes the slide to the next one.
 */
function nextSlide(nextButton) {
  // The parentElement of the button is the slider container
  changeSlide(1, nextButton.parentElement);
}

/**
 * Shows a random slide.
 */
function randomSlide(randomButton) {
  // The nextElementSibling of the random button is the slider container
  const slider = randomButton.nextElementSibling;
  const {totalSlides} = hideAllSlides(slider);
  const slideToShow = slider.children[Math.floor(Math.random() * totalSlides)];
  removeClass(slideToShow, "hidden");
}

/**
 * Initializes the comments view.
 */
function getComments() {
  fetchTotalPage();
  fetchComments();
}

/**
 * Fetches the total comments pages from the server and adds it to the DOM.
 */
function fetchTotalPage() {
  fetch('/comments/total')
   .then(handleTextResponse)
   .then(addTotalToDom)
   .catch(console.error);
}

/**
 * Fetches comments from the server and adds it to the DOM.
 */
function fetchComments() {
  fetch('/comments/data?' + new URLSearchParams({
     limit : getCommentsLimit(),
     offset : getCommentsOffset(),
     language : getCommentsLanguage(),
   }))
   .then(handleJSONResponse)
   .then(addCommentsToDom)
   .catch(console.error);
}

/**
 * Returns the value converted to int if valid and non negative, or the default value
 */
function toInt(value, defaultValue) {
  let intValue = parseInt(value);
  if (isNaN(intValue) || intValue < 0) {
    // if the value is not valid, sets it to the default value
    intValue = defaultValue;
  }
  return intValue;
}

/**
 * Returns the value selected for the limit of comments to show.
 */
function getCommentsLimit() {
  return toInt(document.getElementById("comment-limit").value, DEFAULT_COMMENT_LIMIT);
}

/**
 * Returns the value selected for the limit of comments to show.
 */
function getCommentsOffset() {
  return getCommentsLimit() * (getCurrentPage() - 1);
}

/**
 * Returns the language selected for the comments.
 */
function getCommentsLanguage() {
  return document.getElementById("comment-language").value;
}

/**
 * Handles response by checking it and converting it to text.
 */
function handleTextResponse(response) {
  if (!response.ok) {
    throw new Error("Response error while fetching data: " + 
     response.status + " (" + response.statusText + ")");
  }
  return response.text();
}

/**
 * Handles response by checking it and converting it from json.
 */
function handleJSONResponse(response) {
  if (!response.ok) {
    throw new Error("Response error while fetching data: " + 
     response.status + " (" + response.statusText + ")");
  }
  return response.json();
}

/** 
 * Adds total pages to the DOM. 
 */
function addTotalToDom(totalComments) {
  const pages = toInt(totalComments, 0) / getCommentsLimit();
  document.getElementById('total-pages').innerText = Math.ceil(pages);
  disablePaginationButtons();
}

/** 
 * Adds comments to the DOM. 
 */
function addCommentsToDom(comments) {
  const commentsContainer = document.getElementById('comments-container');
  commentsContainer.innerHTML = "";
  for (const comment of comments) {
    commentsContainer.appendChild(createCommentElement(comment));
  }
}

/**
 * Creates a DOM element of the given type and adds the class to it.
 */
function createDOMElement(type, classAttribute) {
  const element = document.createElement(type);
  addClass(element, classAttribute);
  return element;
}

/**
 * Creates the div element containing the comment.
 */
function createCommentElement(comment) {
  const commentElement = createDOMElement("div", "comment");

  const dateElement = createDOMElement("div", "date");
  dateElement.innerText = comment.date;
  commentElement.appendChild(dateElement);

  const authorElement = createDOMElement("span", "author-name");
  authorElement.innerText = comment.author + ": ";
  commentElement.appendChild(authorElement);

  const commentTextElement = createDOMElement("span", "comment-text");
  commentTextElement.innerText = comment.commentContent;
  commentElement.appendChild(commentTextElement);

  return commentElement;
}

/**
 * Disables some of the pagination buttons if the current page
 * is the first or the last one.
 */
function disablePaginationButtons() {
  let disable = getCurrentPage() === 1;
  document.getElementById("first-page").disabled = disable;
  document.getElementById("prev-page").disabled = disable;
  
  disable = getCurrentPage() === getTotalPage();
  document.getElementById("last-page").disabled = disable;
  document.getElementById("next-page").disabled = disable;
}

/**
 * Returns the current number of the comments page.
 */
function getCurrentPage() {
  const currentPage = document.getElementById("current-page").textContent;
  return toInt(currentPage, 1);
}

/**
 * Returns the total number of the comments page.
 */
function getTotalPage() {
  const totalPage = document.getElementById("total-pages").textContent;
  return toInt(totalPage, 1);
}

/** 
 * Sets the current page.
 */
function setPage(page) {
  if (page < 1 || page > getTotalPage()) {
    page = 1;
  }
  document.getElementById("current-page").innerText = page;
  fetchComments();
  disablePaginationButtons();
}

/**
 * Changes the page to the current one + offset.
 */
function changePage(offset) {
  setPage(getCurrentPage() + offset);
}

/**
 * Sets the current page to the last one.
 */
function setLastPage() {
  setPage(getTotalPage());
}

/**
 * Updates the comments section when the limit changes.
 */
function changeLimit() {
  setPage(1);
  getComments();
}

/**
 * Sets the current page to the number inserted if it is valid
 */
function goToPage() {
  const page = toInt(document.getElementById("to-page").value, 0);
  if (page >= 1 && page <= getTotalPage()) {
    setPage(page);
  }
}

/**
 * Fetches the login status of the user from the server and 
 * shows/hides the comment form.
 */
function fetchLoginStatus() {
  fetch('/login/status')
   .then(handleJSONResponse)
   .then(toggleCommentForm)
   .catch(console.error);
}

/**
 * Shows/hides the comment form and the login link.
 */
function toggleCommentForm(loginStatus) {
  const commentForm = document.getElementById("comment-form");
  const loginLink = document.getElementById("login-link");
  if (loginStatus.isLoggedIn) {
    removeClass(commentForm, "hidden");
    addClass(loginLink, "hidden");
  } else {
    addClass(commentForm, "hidden");
    loginLink.href = loginStatus.loginURL;
    removeClass(loginLink, "hidden");
  }
}

/**
 * Initializes the comments section.
 */
function commentsInit() {
  getComments();
  fetchLoginStatus();
}

/**
 * Initializes the map.
 */
function mapInit() {
  const homeIsolabellaCoords = { lat: 44.9067176, lng: 7.9096066 };
  const universityCoords = { lat: 45.0623602, lng: 7.6599514 };

  const map = new google.maps.Map(document.getElementById("map-container"), {
    center: homeIsolabellaCoords,
    zoom: 10
  });

  addLandmark(homeIsolabellaCoords, map, "H", "This is my hometown and where I live now.");
  addLandmark(universityCoords, map, "U", 
    "This is the Polytechnic of Turin, where I' m studying computer engineering.");
}

/**
 * Adds a marker that shows an info window when clicked.
 */
function addLandmark(location, map, label, description) {
  const marker = new google.maps.Marker({
    position: location,
    label: label,
    map: map
  });
  const infoWindow = new google.maps.InfoWindow({content: description});
  marker.addListener('click', () => {
    infoWindow.open(map, marker);
  });
}