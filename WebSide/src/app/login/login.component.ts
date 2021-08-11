import { Component, OnInit } from '@angular/core';
import {AppComponent} from '../app.component';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private fb : FormBuilder,
              private http : HttpClient) { }

  form : FormGroup = this.fb.group({
    inUsuario: [],
    inSenha: []
  })

  usuario : any = {
    id : 0,
    nome : '',
    usuario: '',
    funcao: ''
  }

  mensagem : string;

  ngOnInit() {
  }


  doLogin() {

    this.http.post('http://localhost:8080/usuario', {
      usuario: this.form.get('inUsuario').value,
      senha: this.form.get('inSenha').value
    }).subscribe( user => {
      if(user == null)
        this.mensagem = 'Usuario e/ou senha incorrento'
      else {
        this.usuario = user;
        localStorage.setItem("user", JSON.stringify({
          id: this.usuario.id,
          nome: this.usuario.nome,
          usuario: this.usuario.usuario,
          funcao: this.usuario.funcao
        }))
        location.href = location.origin
      }
    })


  }

}
