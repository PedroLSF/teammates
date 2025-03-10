import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TimeFormat, getDefaultTimeFormat } from '../../../types/datetime-const';

/**
 * Time picker with fixed time to pick.
 */
@Component({
  selector: 'tm-timepicker',
  templateUrl: './timepicker.component.html',
  styleUrls: ['./timepicker.component.scss'],
})
export class TimepickerComponent {

  @Input()
  isDisabled: boolean = false;

  @Input()
  time: TimeFormat = getDefaultTimeFormat();

  @Output()
  timeChange: EventEmitter<TimeFormat> = new EventEmitter();

  /**
   * Triggers time change event.
   */
  triggerTimeChange(newTime: TimeFormat): void {
    this.timeChange.emit(newTime);
  }

  /**
   * Helper function to create a range.
   */
  range(start: number, end: number): number[] {
    const arr: number[] = [];
    for (let i: number = start; i < end; i += 1) {
      arr.push(i);
    }
    return arr;
  }

  /**
   * Compares two TIMEs.
   *
   * <p>Checks whether they are equal or not.
   */
  timeCompareFn(t1: TimeFormat, t2: TimeFormat): boolean {
    return t1 && t2 && t1.hour === t2.hour && t1.minute === t2.minute;
  }

  /**
   * Checks whether the time is in the fixed list to select.
   */
  isInFixedList(time: TimeFormat): boolean {
    return (time.hour >= 1 && time.hour <= 23 && time.minute === 0)
        || (time.hour === 23 && time.minute === 59);
  }

  /**
   * Formats number {@code i} and pads leading zeros if its digits are less than {@code n}.
   *
   * <p>e.g. n = 2, i = 1 => "01"
   */
  addLeadingZeros(n: number, i: number): string {
    return ('0'.repeat(n) + i).slice(-n);
  }
}
