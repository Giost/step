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
import com.google.sps.data.CommentsStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

/** Servlet that returns and saves the comments. */
@WebServlet("/comments/data")
public class DataServlet extends HttpServlet {
  public static final int DEFAULT_COMMENT_LIMIT = 5;
  public static final int DEFAULT_COMMENT_OFFSET = 0;

  /**
   * Converts a List instance into a JSON string.
   */
  private String convertListToJson(List<Comment> comments) {
    Gson gson = new GsonBuilder().setDateFormat(DateFormat.LONG).create();
    String json = gson.toJson(comments);
    return json;
  }

  /**
   * Returns the value of the limit parameter if valid, 
   * otherwise the default value.
   */
  private int getLimit(HttpServletRequest request) {
    return getNonNegativeIntParameter(request, "limit", DEFAULT_COMMENT_LIMIT);
  }

  /**
   * Returns the value of the offset parameter if valid, 
   * otherwise the default value.
   */
  private int getOffset(HttpServletRequest request) {
    return getNonNegativeIntParameter(request, "offset", DEFAULT_COMMENT_OFFSET);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON);
    response.getWriter().println(
      convertListToJson(
        CommentsStore.load(getLimit(request), getOffset(request))
      )
    );
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
   * @return the integer value of the request parameter, or the default value if the parameter
   *         was not specified by the client or invalid
   */
  private int getIntParameter(HttpServletRequest request, String name, int defaultValue) {
    int parameter;
    try {
      parameter = Integer.parseInt(request.getParameter(name));
    } catch (Exception e) {
      // if there is an error, returns the default value
      parameter = defaultValue;
    }
    return parameter;
  }

  /**
   * @return the integer value of the request parameter, or the default value if the parameter
   *         was not specified by the client or invalid or negative
   */
  private int getNonNegativeIntParameter(HttpServletRequest request, String name, int defaultValue) {
    int parameter = getIntParameter(request, name, defaultValue);
    return (parameter >= 0 ? parameter : defaultValue);
  }

  /**
   * Saves the comment inserted in the form.
   */
  private void addComment(HttpServletRequest request) {
    String commentContent = getParameter(request, "comment-content", "");
    // doesn't insert empty comment
    if (!commentContent.isEmpty()) {
      CommentsStore.save(
        new Comment(getParameter(request, "author", "Anonymous"),
                    commentContent,
                    new Date())
      );
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    addComment(request);
    response.sendRedirect("/index.html");
  }
}
