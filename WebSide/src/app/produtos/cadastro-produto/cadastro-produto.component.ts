import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {stringify} from '@angular/compiler/src/util';
import {Location} from '@angular/common';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-cadastro-produto',
  templateUrl: './cadastro-produto.component.html',
  styleUrls: ['./cadastro-produto.component.css']
})
export class CadastroProdutoComponent implements OnInit {

  params : any;

  constructor(private http: HttpClient,
              private fb: FormBuilder,
              private location: Location,
              private route : ActivatedRoute) {
      this.route.params.subscribe( params => this.params = params)
  }

  fabricantes: any;

  produto : any = {
    id : -1,
    nome : '',
    corPredominante: '',
    fabricante: null,
    descricao: ''
  };

  form: FormGroup = this.fb.group({
    inNome : [],
    inCorPredominante : [],
    inFabricante : [-1],
    inDescricao : []
  });

  ngOnInit() {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    this.http.post('http://localhost:8080/fabricante/findAll', {nome : ''})
      .subscribe(value => {
        this.fabricantes = value;

        this.form.get('inFabricante').setValue(this.produto.fabricante.id);
      });

    if (this.params['id'] != undefined){
      let params: HttpParams = new HttpParams().set('id', this.params['id'])

      this.http.get('http://localhost:8080/produto/findById', {params, headers})
        .subscribe(value => {
          this.produto = value;

          this.form.get('inNome').setValue(this.produto.nome);
          this.form.get('inCorPredominante').setValue(this.produto.corPredominante);
          this.form.get('inFabricante').setValue(this.produto.fabricante == null ? -1 : this.produto.fabricante.id);
          this.form.get('inDescricao').setValue(this.produto.descricao);
        });
    }

  }

  salvar(evt) {

    this.produto.nome = this.form.get('inNome').value;
    this.produto.corPredominante = this.form.get('inCorPredominante').value;
    this.produto.fabricante = this.fabricantes.filter((value) => value.id = this.form.get('inFabricante').value)[0];
    this.produto.descricao = this.form.get('inDescricao').value;

    this.http.put('http://localhost:8080/produto', this.produto)
      .subscribe((next) => {this.location.back(); });


  }

  cancelar(evt){
    this.location.back();
  }

}
