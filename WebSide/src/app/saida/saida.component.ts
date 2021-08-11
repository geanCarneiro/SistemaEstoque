import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {FormBuilder, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-saida',
  templateUrl: './saida.component.html',
  styleUrls: ['./saida.component.css']
})
export class SaidaComponent implements OnInit {

  constructor(private http: HttpClient) { }

  saidas: any;

  filter = {
    produto : null
  }

  ngOnInit() {
    this.reload()
  }

  cancelarSaida(saida) {

    if (confirm("Tem certeza que deseja cancelar a saida de " + saida.quantidade + " " + saida.produto.desc + " do fabricante " + saida.produto.fabricante.nome + "?")) {
      this.http.post('http://localhost:8080/saida/cancelarSaida', saida)
        .subscribe(next => this.reload());
    }


  }

  reload(){

    this.http.post('http://localhost:8080/saida/findAll', this.filter).subscribe(value => this.saidas = value);
  }

}
