import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';
import { ProdutosComponent } from './produtos/produtos.component';
import { FabricantesComponent } from './fabricantes/fabricantes.component';
import { CadastroProdutoComponent } from './produtos/cadastro-produto/cadastro-produto.component';
import { CadastroFabricanteComponent } from './fabricantes/cadastro-fabricante/cadastro-fabricante.component';
import { EstoqueComponent } from './estoque/estoque.component';
import { CadastroEstoqueComponent } from './estoque/cadastro-estoque/cadastro-estoque.component';
import { SaidaComponent } from './saida/saida.component';
import { MenuComponent } from './menu/menu.component';
import {LoginComponent} from './login/login.component';
import { UsuariosComponent } from './usuarios/usuarios.component';
import { CadastroUsuariosComponent } from './usuarios/cadastro-usuarios/cadastro-usuarios.component';

const routes : Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'estoque'},
  {path: 'login', component: LoginComponent},
  {path: 'produtos', component: ProdutosComponent},
  {path: 'produtos/edit', component: CadastroProdutoComponent},
  {path: 'produtos/edit/:id', component: CadastroProdutoComponent},
  {path: 'fabricantes', component: FabricantesComponent},
  {path: 'fabricantes/edit', component: CadastroFabricanteComponent},
  {path: 'fabricantes/edit/:id', component: CadastroFabricanteComponent},
  {path: 'estoque', component: EstoqueComponent},
  {path: 'estoque/:operacao/:idProduto', component: CadastroEstoqueComponent},
  {path: 'saidas', component: SaidaComponent},
  {path: 'usuarios', component: UsuariosComponent},
  {path: 'usuarios/edit', component: CadastroUsuariosComponent},
  {path: 'usuarios/edit/:id', component: CadastroUsuariosComponent},
]

@NgModule({
  declarations: [
    AppComponent,
    ProdutosComponent,
    FabricantesComponent,
    CadastroProdutoComponent,
    CadastroFabricanteComponent,
    EstoqueComponent,
    CadastroEstoqueComponent,
    SaidaComponent,
    MenuComponent,
    LoginComponent,
    UsuariosComponent,
    CadastroUsuariosComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
