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

package com.google.sps.data;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import com.google.sps.data.Comment;

/** Class that allows to perform operations to the comments in the database. */
public class CommentsStore {
  /**
   * Saves the comment in the database.
   */
  public static void save(Comment comment) {
    ofy().save().entity(comment).now();
  }

  /**
   * Loads and returns all the comments in the database sorted by date.
   */
  public static List<Comment> load() {
    return load(0, 0);
  }

  /**
   * Loads and returns comments in the database sorted by date and limited in number.
   */
  public static List<Comment> load(int limit) {
    return load(limit, 0);
  }

  /**
   * Loads and returns comments in the database sorted by date starting from the
   * offset (included) and limited in number.
   */
  public static List<Comment> load(int limit, int offset) {
    return ofy().load().type(Comment.class).order("-date").offset(offset).limit(limit).list();
  }

  /**
   * Returns the total number of comments.
   */
  public static int totalComments() {
    return ofy().load().type(Comment.class).count();
  }
}