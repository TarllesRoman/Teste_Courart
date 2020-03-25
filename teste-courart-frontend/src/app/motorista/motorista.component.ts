import { Motorista } from './../model/motorista';
import { MotoristaService } from './motorista.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-motorista',
  templateUrl: './motorista.component.html',
  styleUrls: ['./motorista.component.css']
})
export class MotoristaComponent implements OnInit {

  formulario: FormGroup;
  formRelatorio: FormGroup;
  lista: Motorista[];
  aparecerMsg = false;

  list = true;
  report = false;
  register = false;

  remover = false;

  constructor(private service: MotoristaService, private builder: FormBuilder) { }

  ngOnInit() {
    this.rstFormulario();
    this.listar();
    this.formRelatorio = this.builder.group(
      {
        data1: [null, [Validators.required, Validators.maxLength(10)]],
        data2: [null, [Validators.required, Validators.maxLength(10)]]
      });
  }

  // resetar formulario
  rstFormulario() {
    if (this.formulario != null) {
      this.formulario.reset();
    }
    this.formulario = this.builder.group(
      {
        codigo: [null],
        nome: [null, [ Validators.required, Validators.maxLength(40)] ],
        cpf: [null, Validators.required],
        dataNascimento: [ null, [Validators.required, Validators.maxLength(10)] ],
        login: [null, [ Validators.required, Validators.maxLength(12)] ],
        senha: [null, [ Validators.required, Validators.maxLength(12)] ]
      }
    );
  }

  // resetar formulario
  setFormulario(motorista: Motorista) {
    if (this.formulario != null) {
      this.formulario.reset();
    }

    this.formulario.patchValue(motorista);
  }

  salvar() {
    const motorista = this.formulario.value as Motorista;

    this.service.criar(motorista).subscribe(
      retorno => {
        console.dir(retorno, { depth: null });
        this.aparecerMsg = true;
        this.rstFormulario();
      }
    );
  }

  listar() {
    this.service.listar().subscribe(
      retorno => {
        this.lista = retorno;
      },
      error => {
        console.dir(error, { depth: null });
      }
    );
  }

  atualizar() {
    const motorista = this.formulario.value as Motorista;

    this.service.atualizar(motorista.codigo, motorista).subscribe(
      retorno => {
        // console.dir(retorno, { depth: null });
        this.aparecerMsg = true;
        this.setFormulario(retorno);
      }
    );
  }

  cancelar() {
    this.rstFormulario();
  }

  excluir() {
    const motorista = this.formulario.value as Motorista;

    this.service.remover(motorista.codigo).subscribe(
      retorno => {
        this.remover = false;
        this.showList();
      }
    );
  }

  aniversariantes(data1: string, data2: string) {

    this.service.buscarAniversariantes(data1, data2).subscribe(
      retorno => {
        console.dir('SUCCESS', { depth: null });
        const file = new Blob([retorno], { type: 'application/pdf' });
        const fileURL = URL.createObjectURL(file);
        window.open(fileURL);
      }
    );
  }

  showList() {
    this.listar();

    this.list = true;
    this.report = false;
    this.register = false;
  }

  showRegister() {
    this.list = false;
    this.report = false;
    this.register = true;

    this.aparecerMsg = false;
    this.rstFormulario();
  }

  showReport() {
    this.list = false;
    this.report = true;
    this.register = false;
    if (this.formRelatorio != null) {
      this.formRelatorio.reset();
    }
  }

  update(motorista: Motorista) {
    this.list = false;
    this.report = false;
    this.register = true;

    this.aparecerMsg = false;
    this.setFormulario(motorista);
  }

  buscarCPF(cpf: string) {
    this.service.buscarCPF(cpf).subscribe(
      retorno => {
        console.dir(retorno, { depth: null });
        this.lista = retorno;
      },
      error => {
        console.dir(error, { depth: null });
      }
    );
  }

  buscarNome(nome: string) {
    this.service.buscarNome(nome).subscribe(
      retorno => {
        console.dir(retorno, { depth: null });
        this.lista = retorno;
      },
      error => {
        console.dir(error, { depth: null });
      }
    );
  }

}
