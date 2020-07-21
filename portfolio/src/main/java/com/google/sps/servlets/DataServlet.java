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

package com.google.sps.servlets;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

/** Servlet that returns some example content. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private List<String> comments = new ArrayList<>(
    Arrays.asList("Nice!",
                  "Very interesting.",
                  "I would like to know more about your favourite TV shows!"));

  /**
   * Converts a List instance into a JSON string.
   */
  public static String convertListToJson(List<String> list) {
    StringBuilder jsonBuilder = new StringBuilder();
    jsonBuilder.append("[");
    for (int i = 0; i < list.size(); i++) {
      jsonBuilder.append("\"" + list.get(i) + "\"");
      if (i < list.size() - 1) {
        jsonBuilder.append(", ");
      }
    }
    jsonBuilder.append("]");
    return jsonBuilder.toString();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON);
    response.getWriter().println(convertListToJson(comments));
  }
}
