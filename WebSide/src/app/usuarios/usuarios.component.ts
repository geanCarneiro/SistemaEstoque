import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {FormBuilder, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.css']
})
export class UsuariosComponent implements OnInit {

  constructor(private http: HttpClient,
              private fb: FormBuilder) { }

  fabricantes: any;
  usuarios: any;

  filter : any = {
    id: -1,
    nome: '',
    usuario: '',
    funcao: null
  }

  ngOnInit() {
    this.reload();
  }

  remover(usuario) {

    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: usuario,
    };

    this.http.delete('http://localhost:8080/produto', options)
      .subscribe(next => this.reload());

  }

  reload(){

    this.http.post('http://localhost:8080/usuario/findAll', this.filter).subscribe(value => this.usuarios = value);
  }
}
