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

package com.google.sps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> availableSlots = new ArrayList<>();
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return availableSlots;
    }
    List<TimeRange> availableSlotsOptional = new ArrayList<>();
    availableSlots.add(TimeRange.WHOLE_DAY);
    availableSlotsOptional.add(TimeRange.WHOLE_DAY);

    Collection<String> attendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();
    long requiredDuration = request.getDuration();
    for (Event event : events) {
      TimeRange eventTimeRange = event.getWhen();
      boolean containsAttendee = containsAttendee(attendees, event);
      boolean containsOptionalAttendee = containsAttendee(optionalAttendees, event);
      if (!containsAttendee && !containsOptionalAttendee) {
        continue;
      }
      if (containsAttendee) {
        removeTimeRange(availableSlots.listIterator(), eventTimeRange, requiredDuration);
      }
      removeTimeRange(availableSlotsOptional.listIterator(), eventTimeRange, requiredDuration);
    }
    if (availableSlotsOptional.size() > 0) {
      return availableSlotsOptional;
    }
    return availableSlots;
  }

  /**
   * Removes the event time range from the available slots in the list.
   */
  private void removeTimeRange(
          ListIterator<TimeRange> listIterator, TimeRange eventTimeRange, long requiredDuration) {
    while (listIterator.hasNext()) {
      TimeRange availableTimeRange = listIterator.next();
      if (!eventTimeRange.overlaps(availableTimeRange)) {
        continue;
      }
      listIterator.remove();
      if (!eventTimeRange.contains(availableTimeRange)) {
        if (availableTimeRange.contains(eventTimeRange)) {
          splitRangeAvailable(availableTimeRange, eventTimeRange,
                  listIterator, requiredDuration);
        } else {
          TimeRange newTimeRange = removeEventTimeRange(availableTimeRange, eventTimeRange);
          addListCheckDuration(listIterator, newTimeRange, requiredDuration);
        }
      }
    }
  }

  /**
   * Returns true if the event contains at least one of the attendees,
   * otherwise false.
   */
  private boolean containsAttendee(Collection<String> attendees, Event event) {
    return attendees.stream()
            .anyMatch((attendee) -> event.getAttendees().contains(attendee));
  }

  /**
   * Returns the available time range without the overlapping time with the event time range
   * in the cases where one time range doesn't contain the other.
   */
  private TimeRange removeEventTimeRange(TimeRange availableTimeRange, TimeRange eventTimeRange) {
    int start = (availableTimeRange.start() <= eventTimeRange.start() ?
            availableTimeRange.start() : eventTimeRange.end());
    int end = (availableTimeRange.end() >= eventTimeRange.end() ?
            availableTimeRange.end() : eventTimeRange.start());
    return TimeRange.fromStartEnd(start, end, false);
  }

  /**
   * Adds the time range to the list if its duration is greater or equal to the required duration.
   */
  private void addListCheckDuration(ListIterator<TimeRange> listIterator, TimeRange timeRange, long requiredDuration) {
    if (timeRange.duration() >= requiredDuration) {
      listIterator.add(timeRange);
    }
  }

  /**
   * Given an available time range that contains an event, removes the event time range and adds
   * the two new time ranges to the list.
   */
  private void splitRangeAvailable(
          TimeRange availableTimeRange, TimeRange eventTimeRange,
          ListIterator<TimeRange> listIterator, long requiredDuration) {
    TimeRange firstSlot = TimeRange.fromStartEnd(availableTimeRange.start(), eventTimeRange.start(), false);
    TimeRange lastSlot = TimeRange.fromStartEnd(eventTimeRange.end(), availableTimeRange.end(), false);
    addListCheckDuration(listIterator, firstSlot, requiredDuration);
    addListCheckDuration(listIterator, lastSlot, requiredDuration);
  }
}
