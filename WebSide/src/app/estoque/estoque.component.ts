import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {FormBuilder, FormGroup} from '@angular/forms';
import {AppComponent} from '../app.component';
import {Location} from '@angular/common';

@Component({
  selector: 'app-estoque',
  templateUrl: './estoque.component.html',
  styleUrls: ['./estoque.component.css']
})
export class EstoqueComponent implements OnInit {

  constructor(private http: HttpClient,
              private location : Location,
              private fb: FormBuilder) { }

  produtos: any;
  estoque: any;

  orderByDiferenca: boolean;

  form: FormGroup = this.fb.group({
    inProduto : []
  });

  filter : any = {
    produto : {}
  }

  ngOnInit() {


    this.http.post('http://localhost:8080/produto/findAll', {})
      .subscribe(value => {
        this.produtos = value;

      });
    this.reload(true)
  }

  remover(estoque) {

    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: estoque,
    };

    this.http.delete('http://localhost:8080/estoque', options)
      .subscribe(next => this.reload());

  }

  reload(fistLoad : boolean = false){

    this.http.post('http://localhost:8080/estoque/findAll', this.filter).subscribe(value => {
      this.estoque = value

      let escolha = false;
      if(fistLoad && this.estoque.filter(item => (item.estoqueIdeal - item.quantidade) < 0 ).length < 0) {
        escolha = confirm("Existem produtos com o estoque abaixo do ideal. [OK] para ordenar esses produtos primeiro ou [CANCELAR] para exibir na ordem normal");
        this.orderByDiferenca = escolha;
      }

      if(this.orderByDiferenca)
        this.estoque.sort((item1, item2) => (item2.estoqueIdeal - item2.quantidade) - (item1.estoqueIdeal - item1.quantidade))
    });
  }

  filtrar(){

    this.filter.produto.nome = this.form.get('inProduto').value

    this.reload();

  }

}
