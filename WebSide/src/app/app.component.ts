import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {FormControl} from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(private http: HttpClient) {
  }

  static user : any;

  ngOnInit(): void {
    AppComponent.user = JSON.parse(localStorage.getItem("user"))

    if(AppComponent.user == null && location.pathname != '/login')
      location.href = location.origin + '/login'
    else if(AppComponent.user != null && location.pathname == '/login')
      location.href = location.origin

  }

  getLogado(){
    return AppComponent.user != null
  }

}
