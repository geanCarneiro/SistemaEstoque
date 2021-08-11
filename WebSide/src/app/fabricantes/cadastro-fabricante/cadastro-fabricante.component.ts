import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Location} from '@angular/common';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-cadastro-fabricante',
  templateUrl: './cadastro-fabricante.component.html',
  styleUrls: ['./cadastro-fabricante.component.css']
})
export class CadastroFabricanteComponent implements OnInit {

  params : any;

  constructor(private http: HttpClient,
              private fb: FormBuilder,
              private location: Location,
              private route : ActivatedRoute) {
    this.route.params.subscribe( params => this.params = params)
  }

  fabricante : any = {
    id : -1,
    nome : ''
  };

  form: FormGroup = this.fb.group({
    inNome : []
  });

  ngOnInit() {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');

    if (this.params['id'] != undefined){
      let params: HttpParams =  new HttpParams().set('id', this.params['id'])

      this.http.get('http://localhost:8080/fabricante/findById', {params, headers})
        .subscribe(value => {
          this.fabricante = value;

          this.form.get('inNome').setValue(this.fabricante.nome);
        });
    }

  }

  salvar(evt) {

    this.fabricante.nome = this.form.get('inNome').value;

    this.http.put('http://localhost:8080/fabricante', this.fabricante)
      .subscribe((next) => {this.location.back(); });

  }

  cancelar(evt){
    this.location.back();
  }

}
