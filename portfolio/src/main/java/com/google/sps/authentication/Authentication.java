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

package com.google.sps.authentication;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Authentication {
  /**
   * Returns the user information.
   */
  public static UserService getUser() {
    return UserServiceFactory.getUserService();
  }
  /**
   * Returns true if the user is logged in, false otherwise.
   */
  public static boolean isLoggedIn() {
    return getUser().isUserLoggedIn();
  }

  /**
   * Returns a login URL that redirects to the index page.
   */
  public static String loginURL() {
    return getUser().createLoginURL("/index.html");
  }

  /**
   * Returns the email address of the current user.
   */
  public static String getEmail() {
    return getUser().getCurrentUser().getEmail();
  }
}