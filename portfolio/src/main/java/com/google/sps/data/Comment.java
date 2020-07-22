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

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import java.util.Date;

/** Class representing a comment. */
@Entity
public class Comment {
  @Id private Long id;
  private String author;
  private String commentContent;
  @Index private Date date;

  public Comment(String author, String commentContent, Date date) {
    this.author = author;
    this.commentContent = commentContent;
    this.date = date;
  }

  public Comment() {
  }

  public Long getId() {
    return id;
  }

  public String getAuthor() {
    return author;
  }

  public String getCommentContent() {
    return commentContent;
  }

  public Date getDate() {
    return date;
  }
}