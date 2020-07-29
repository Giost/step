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

package com.google.sps.translation;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.cloud.translate.Language;
import com.google.sps.data.Comment;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Class that allows to translate. */
public class TranslationProvider {
  public static final String DEFAULT_TARGET_LANGUAGE = "en";
  private Translate translate;
  private Set<String> supportedLanguageCodes;

  public TranslationProvider() {
    this.translate = 
      TranslateOptions.newBuilder()
        .setProjectId("gioda-step-2020")
        .setQuotaProjectId("gioda-step-2020")
        .setTargetLanguage(DEFAULT_TARGET_LANGUAGE)
        .build()
        .getService();
    
    List<Language> languages = translate.listSupportedLanguages();
    this.supportedLanguageCodes = 
      languages.stream()
               .map(Language::getCode)
               .collect(Collectors.toSet());
  }

  /**
   * Checks if the language code is supported and if not returns the default
   * targer language, otherwise the original language code.
   */
  private String checkLanguageCode(String languageCode) {
    if (!supportedLanguageCodes.contains(languageCode.toLowerCase())) {
      return DEFAULT_TARGET_LANGUAGE;
    }
    return languageCode;
  }
  
  /**
   * Returns the translated text to the language specified.
   */
  public String translateText(String text, String languageCode) {
    languageCode = checkLanguageCode(languageCode);
    return translate
            .translate(text, Translate.TranslateOption.targetLanguage(languageCode))
            .getTranslatedText();
  }

  /**
   * Returns a new instance of the comment translated in the language specified.
   */
  public Comment translateComment(Comment comment, String languageCode) {
    return
      new Comment(comment.getAuthor(),
                  translateText(comment.getCommentContent(), languageCode),
                  comment.getDate()
      );
  }
}