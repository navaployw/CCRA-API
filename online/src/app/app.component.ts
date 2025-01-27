import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Demo';
  data = {}  as any;
  constructor(private http: HttpClient,public rt : Router ) {
    console.log("TEST################");
    http.get('resource').subscribe(data => this.data = data);
  }

  gotoPage(){
    console.log('go to page')
    this.rt.navigate(["welcome"])  }
}
