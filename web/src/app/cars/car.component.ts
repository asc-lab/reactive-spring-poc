import {ChangeDetectorRef, Component, OnInit} from '@angular/core';

import {Car} from './car';
import {CarReactiveService} from './car.reactive.service';
import {Subscription} from "rxjs";

@Component({
  selector: 'car-component',
  providers: [CarReactiveService],
  templateUrl: './car.component.html'
})
export class CarComponent implements OnInit {
  cars: Car[] = [];
  car: Car;
  mode: String;
  sub: Subscription;

  constructor(private carService: CarReactiveService,
              private changeDetectionRef: ChangeDetectorRef) {
  }

  ngOnInit() {
    this.mode = "none";
  }

  private refreshView() {
    if (!this.changeDetectionRef['destroyed']) {
      this.changeDetectionRef.detectChanges();
    }
  }

  flux() {
    this.cars = [];
    if (this.sub != undefined)
      this.sub.unsubscribe();
    this.sub = this.carService.findAll().subscribe(res => {
      this.cars = res;
      this.refreshView();
    });
  }

  findAllTransient() {
    this.cars = [];
    if (this.sub != undefined)
      this.sub.unsubscribe();
    this.sub = this.carService.findAllTransient().subscribe(res => {
      this.cars = res;
      this.refreshView();
    });
  }

  mono() {
    if (this.sub != undefined)
      this.sub.unsubscribe();
    this.carService.findById(2).subscribe(res => {
      this.car = res;
      this.refreshView();
    });
  }

}
