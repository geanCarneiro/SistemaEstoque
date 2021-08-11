import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {FormBuilder, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-fabricantes',
  templateUrl: './fabricantes.component.html',
  styleUrls: ['./fabricantes.component.css']
})
export class FabricantesComponent implements OnInit {
  constructor(private http: HttpClient,
              private fb: FormBuilder) { }

  fabricantes: any;

  form: FormGroup = this.fb.group({
    inNome : []
  });

  filter : any = {
    id: -1,
    nome: ''
  }
  ngOnInit() {

    this.reload();
  }

  remover(fabricante) {

    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: fabricante,
    };

    this.http.delete('http://localhost:8080/fabricante', options)
      .subscribe(next => this.reload());

  }

  reload(){

    this.http.post('http://localhost:8080/fabricante/findAll', this.filter)
      .subscribe(value => {
        this.fabricantes = value;
      });
  }

  filtrar(){

    this.filter.nome = this.form.get('inNome').value

    this.reload();

  }

}
