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

/**
 * Check the validity of the parameters.
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
 * Check if the element has the class.
 */
function hasClass(element, classAttribute) {
  check(element, classAttribute, "hasClass");
  return element.classList.contains(classAttribute);
}

/**
 * Add the class in the element.
 */
function addClass(element, classAttribute) {
  check(element, classAttribute, "addClass");
  element.classList.add(classAttribute);
}

/**
 * Remove the class in the element.
 */
function removeClass(element, classAttribute) {
  check(element, classAttribute, "removeClass");
  element.classList.remove(classAttribute);
}

/**
 * Insert/remove the class in the element.
 */
function toggleClass(element, classAttribute) {
  if (hasClass(element, classAttribute)) {
    removeClass(element, classAttribute);
  } else {
    addClass(element, classAttribute);
  }
}

/**
 * Show/hide the description inside the section.
 */
function toggleDescription(sectionId) {
  const sectionElement = document.getElementById(sectionId);
  if (!sectionElement) {
    throw new Error("toggleDescription wrong sectionId.");
  }
  toggleClass(sectionElement, "collapsed");
}

/**
 * Hide the currently displayed slide and return the total number of slides
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
 * Change the slide to the current one + offset.
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
 * Change the slide to the previous one.
 */
function previousSlide(previousButton) {
  // The parentElement of the button is the slider container
  changeSlide(-1, previousButton.parentElement);
}

/**
 * Change the slide to the next one.
 */
function nextSlide(nextButton) {
  // The parentElement of the button is the slider container
  changeSlide(1, nextButton.parentElement);
}

/**
 * Show a random slide.
 */
function randomSlide(randomButton) {
  // The nextElementSibling of the random button is the slider container
  const slider = randomButton.nextElementSibling;
  const {totalSlides} = hideAllSlides(slider);
  const slideToShow = slider.children[Math.floor(Math.random() * totalSlides)];
  removeClass(slideToShow, "hidden");
}