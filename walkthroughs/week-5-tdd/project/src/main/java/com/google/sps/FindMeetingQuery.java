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

    availableSlots.add(TimeRange.WHOLE_DAY);
    for (Event event : events) {
      TimeRange eventTimeRange = event.getWhen();
      boolean containsAttendee =
              request.getAttendees().stream().anyMatch((attendee) -> event.getAttendees().contains(attendee));
      if (containsAttendee) {
        ListIterator<TimeRange> availableSlotsIterator = availableSlots.listIterator();
        while (availableSlotsIterator.hasNext()) {
          TimeRange availableTimeRange = availableSlotsIterator.next();
          if (eventTimeRange.overlaps(availableTimeRange)) {
            availableSlotsIterator.remove();
            if (!eventTimeRange.contains(availableTimeRange)) {
              if (availableTimeRange.contains(eventTimeRange)) {
                splitRangeAvailable(availableTimeRange, eventTimeRange,
                        availableSlotsIterator, request.getDuration());
              } else {
                TimeRange newTimeRange = removeEventTimeRange(availableTimeRange, eventTimeRange);
                addListCheckDuration(availableSlotsIterator, newTimeRange, request.getDuration());
              }
            }
          }
        }
      }
    }
    return availableSlots;
  }

  /**
   * Returns the available time range without the overlapping time with the event time range.
   */
  private TimeRange removeEventTimeRange(TimeRange availableTimeRange, TimeRange eventTimeRange) {
    int start = (availableTimeRange.start() <= eventTimeRange.start() ?
            availableTimeRange.start() : eventTimeRange.end());
    int end = (availableTimeRange.end() >= eventTimeRange.end() ?
            availableTimeRange.end() : eventTimeRange.start());
    return TimeRange.fromStartEnd(start, end, false);
  }

  /**
   * Adds the time range to the list if its duration is greater or equal to the passed duration.
   */
  private void addListCheckDuration(ListIterator<TimeRange> listIterator, TimeRange timeRange, long duration) {
    if (timeRange.duration() >= duration) {
      listIterator.add(timeRange);
    }
  }

  /**
   * Given an available time range that contains an event, removes the event time range and adds
   * the two new time ranges to the list.
   */
  private void splitRangeAvailable(
          TimeRange timeRangeAvailable, TimeRange timeRangeEvent,
          ListIterator<TimeRange> listIterator, long duration) {
    TimeRange firstSlot = TimeRange.fromStartEnd(timeRangeAvailable.start(), timeRangeEvent.start(), false);
    TimeRange lastSlot = TimeRange.fromStartEnd(timeRangeEvent.end(), timeRangeAvailable.end(), false);
    addListCheckDuration(listIterator, firstSlot, duration);
    addListCheckDuration(listIterator, lastSlot, duration);
  }
}
