import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Location} from '@angular/common';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-cadastro-estoque',
  templateUrl: './cadastro-estoque.component.html',
  styleUrls: ['./cadastro-estoque.component.css']
})
export class CadastroEstoqueComponent implements OnInit {

  params : any;
  operacao : string;

  constructor(private http: HttpClient,
              private fb: FormBuilder,
              private location: Location,
              private route : ActivatedRoute) {
    this.route.params.subscribe( params => this.params = params)
  }

  itemEstoque : any = {
    id : -1,
    produto : '',
    quantidade: 0,
    estoqueIdeal: 0
  };

  inProduto : string
  inFabricante : string
  inDisponivel : any


  form: FormGroup = this.fb.group({
    inQuantidade : [0]
  });

  ngOnInit() {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');

    this.operacao = this.params['operacao']
    if (this.params['idProduto'] != undefined){
      let params: HttpParams = new HttpParams().set('idProduto', this.params['idProduto'])

      this.http.get('http://localhost:8080/estoque/findByProduto', {params, headers})
        .subscribe(value => {
          this.itemEstoque = value;

          this.inProduto = this.itemEstoque.produto.nome
          this.inFabricante = this.itemEstoque.produto.fabricante.nome
          this.inDisponivel = this.itemEstoque.quantidade
        });
    }

  }

  soma100(){
    const novoValor = this.form.get('inQuantidade').value + 100
      this.form.get('inQuantidade').setValue(this.operacao == 'entrada' ? novoValor : Math.min(novoValor, this.inDisponivel));
  }

  soma10(){
    const novoValor = this.form.get('inQuantidade').value + 10
    this.form.get('inQuantidade').setValue(this.operacao == 'entrada' ? novoValor : Math.min(novoValor, this.inDisponivel));
  }

  soma1(){
    const novoValor = this.form.get('inQuantidade').value + 1;
    this.form.get('inQuantidade').setValue(this.operacao == 'entrada' ? novoValor : Math.min(novoValor, this.inDisponivel));
  }

  set0(){
    this.form.get('inQuantidade').setValue(0);
  }

  subtrai1(){
    this.form.get('inQuantidade').setValue(Math.max(this.form.get('inQuantidade').value - 1, 0));
  }

  subtrai10(){
    this.form.get('inQuantidade').setValue(Math.max(this.form.get('inQuantidade').value - 10, 0));
  }

  subtrai100(){
    this.form.get('inQuantidade').setValue(Math.max(this.form.get('inQuantidade').value - 100, 0));
  }

  salvar(evt) {

    const qt = this.form.get('inQuantidade').value;

    if( this.operacao == 'entrada' ) {
      this.itemEstoque.quantidade += qt
    } else {
      this.itemEstoque.quantidade -= qt
    }

    this.http.put('http://localhost:8080/estoque', this.itemEstoque)
      .subscribe((value) => {
          if( this.operacao == 'saida' ) {
            let saida = {
              produto : this.itemEstoque.produto,
              quantidade: qt,
              data : Date.now()
            }

            this.http.put('http://localhost:8080/saida', saida)
              .subscribe((next) => this.location.back())
          } else {
            this.location.back()
          }

        })

  }

  cancelar(evt){
    this.location.back();
  }

}
