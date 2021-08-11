import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  isGerente(){
    let user = JSON.parse(localStorage.getItem('user'));

    return user.funcao == 'Gerente'
  }

  sair(){
    localStorage.removeItem('user')
    location.href = location.origin + '/login'
  }

}
