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

import com.google.sps.data.Comment;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

/** Servlet that returns some example content. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private final Deque<Comment> comments = new ArrayDeque<>();

  public DataServlet() {
    // adds sample comments
    comments.addFirst(new Comment("Loris", "Nice!", new Date()));
    comments.addFirst(new Comment("Mark", "Very interesting.", new Date()));
  }

  /**
   * Converts a Deque instance into a JSON string.
   */
  private String convertDequeToJson(Deque<Comment> comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON);
    response.getWriter().println(convertDequeToJson(comments));
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null || value.isEmpty()) {
      return defaultValue;
    }
    return value;
  }

  /**
   * Saves the comment inserted in the form.
   */
  private void addComment(HttpServletRequest request) {
    String commentContent = getParameter(request, "comment-content", "");
    // doesn't insert empty comment
    if (!commentContent.isEmpty()) {
      comments.addFirst(
        new Comment(getParameter(request, "author", "Anonymous"),
                    commentContent,
                    new Date()));
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    addComment(request);
    response.sendRedirect("/index.html");
  }
}
