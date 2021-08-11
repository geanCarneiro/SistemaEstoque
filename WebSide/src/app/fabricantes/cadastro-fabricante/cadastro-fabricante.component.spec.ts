import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastroFabricanteComponent } from './cadastro-fabricante.component';

describe('CadastroFabricanteComponent', () => {
  let component: CadastroFabricanteComponent;
  let fixture: ComponentFixture<CadastroFabricanteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CadastroFabricanteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CadastroFabricanteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
