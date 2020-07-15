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
 * Show/hide the description inside the element which has title as id.
 */
function toggleContent(sectionId) {
  if (document.getElementById(sectionId) == null) {
      return;
  }
  const descriptionElement = document.getElementById(sectionId).getElementsByClassName("description");
  if (descriptionElement && descriptionElement.length>0) {
    const classes = descriptionElement[0].className.split(" ");
    if (classes.indexOf("collapsed") === -1) {
      classes.push("collapsed")
    } else {
      classes.splice(classes.indexOf("collapsed"), 1);
    }
    descriptionElement[0].className = classes.join(" ");
  }
}
