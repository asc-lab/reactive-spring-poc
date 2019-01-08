import {Injectable} from '@angular/core';

import {HttpClient} from "@angular/common/http";
import {Car} from "./car";
import {Observable} from "rxjs";

@Injectable()
export class CarReactiveService {


  constructor(private http: HttpClient) {
  }

  url: string = 'http://localhost:7575/cars';

  findById(id: number): Observable<Car> {
    return this.http.get<Car>(this.url + '/' + id);
  }

  findAll(): Observable<Array<Car>> {
    let cars = [];
    return Observable.create((observer) => {
      let eventSource = new EventSource(this.url + '/all');
      eventSource.onmessage = (event) => {
        let json = JSON.parse(event['data']);
        cars.push(new Car(json['id'], json['description']));
        observer.next(cars);
      };
      eventSource.onerror = (error) => observer.error('EventSource error: ' + error);
    });
  }

  findAllTransient(): Observable<Array<Car>> {
    let cars = [];
    return Observable.create((observer) => {
      let eventSource = new EventSource(this.url + '/all-transient');
      eventSource.onmessage = (event) => {
        let json = JSON.parse(event['data']);
        cars.push(new Car(json['id'], json['description']));
        observer.next(cars);
      };
      eventSource.onerror = (error) => observer.error('EventSource error: ' + error);
    });
  }
}
