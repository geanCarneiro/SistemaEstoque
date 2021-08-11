import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Location} from '@angular/common';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-cadastro-usuarios',
  templateUrl: './cadastro-usuarios.component.html',
  styleUrls: ['./cadastro-usuarios.component.css']
})
export class CadastroUsuariosComponent implements OnInit {

  params : any;

  constructor(private http: HttpClient,
              private fb: FormBuilder,
              private location: Location,
              private route : ActivatedRoute) {
    this.route.params.subscribe( params => this.params = params)
  }

  usuario : any = {
    id : -1,
    nome : '',
    usuario: '',
    senha: '',
    funcao: ''
  };

  form: FormGroup = this.fb.group({
    inNome : [],
    inUsuario : [],
    inSenha : [],
    inFuncao: ['']
  });

  mostrarSenha : boolean;

  ngOnInit() {
    if (this.params['id'] != undefined){
      const headers = new HttpHeaders().set('Content-Type', 'application/json');
      let params: HttpParams = new HttpParams().set('id', this.params['id'])

      this.http.get('http://localhost:8080/usuario/findById', {params, headers})
        .subscribe(value => {
          this.usuario = value;

          this.form.get('inNome').setValue(this.usuario.nome);
          this.form.get('inUsuario').setValue(this.usuario.usuario);
          this.form.get('inFuncao').setValue(this.usuario.funcao);
        });
    }

  }

  salvar(evt) {

    this.usuario.nome = this.form.get('inNome').value;
    this.usuario.usuario = this.form.get('inUsuario').value;
    this.usuario.senha = this.form.get('inSenha').value;
    this.usuario.funcao = this.form.get('inFuncao').value;

    this.http.put('http://localhost:8080/usuario', this.usuario)
      .subscribe((next) => {this.location.back(); });


  }

  cancelar(evt){
    this.location.back();
  }

}
