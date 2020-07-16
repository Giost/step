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
* Insert/remove the class in the element.
*/
function toggleClass(element, classAttribute) {
  if (!element || typeof element.className !== "string" || !classAttribute || typeof classAttribute !== "string") {
    return;
  }
  const classes = element.className.split(" ");
  if (classes.indexOf(classAttribute) === -1) {
    classes.push(classAttribute);
  } else {
    classes.splice(classes.indexOf(classAttribute), 1);
  }
  element.className = classes.join(" ");
}

/**
 * Show/hide the description inside the section.
 */
function toggleDescription(sectionId) {
  if (!document.getElementById(sectionId)) {
    return;
  }
  const sectionElement = document.getElementById(sectionId);
  toggleClass(sectionElement, "collapsed");
}
