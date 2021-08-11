import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {FormBuilder, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-produtos',
  templateUrl: './produtos.component.html',
  styleUrls: ['./produtos.component.css']
})
export class ProdutosComponent implements OnInit {

  constructor(private http: HttpClient,
              private fb: FormBuilder) { }

  fabricantes: any;
  produtos: any;

  form: FormGroup = this.fb.group({
    inNome : [],
    inCorPredominante: [],
    inFabricante : [-1]
  });

  filter : any = {
    id: -1,
    nome: '',
    corPredominante: '',
    fabricante: null
  }

  ngOnInit() {
    this.http.post('http://localhost:8080/fabricante/findAll', { nome : ''})
      .subscribe(value => {
        this.fabricantes = value;
      });

    this.reload();

  }
  remover(produto) {

    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: produto,
    };

    this.http.delete('http://localhost:8080/produto', options)
      .subscribe(next => this.reload());

  }

  reload(){

    this.http.post('http://localhost:8080/produto/findAll', this.filter).subscribe(value => this.produtos = value);
  }

  filtrar(){

    this.filter.desc = this.form.get('inNome').value == null ? '' : this.form.get('inNome').value
    this.filter.corPredominante = this.form.get('inCorPredominante').value == null ? '' : this.form.get('inCorPredominante').value
    this.filter.fabricante = this.form.get('inFabricante').value == -1 ? null : this.fabricantes[this.form.get('inFabricante').value]

    this.reload();

  }

}
