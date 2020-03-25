import { VeiculoService } from './../veiculo/veiculo.service';
import { MotoristaService } from './../motorista/motorista.service';
import { Veiculo } from './../model/veiculo';
import { Motorista } from './../model/motorista';
import { DirecaoService } from './direcao.service';
import { Direcao } from './../model/direcao';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-direcao',
  templateUrl: './direcao.component.html',
  styleUrls: ['./direcao.component.css']
})
export class DirecaoComponent implements OnInit {

  formulario: FormGroup;
  formRelatorio: FormGroup;
  lista: Direcao[];

  msgPositiva = true;
  msgNegativa = null;
  remover = false;

  list = true;
  report = false;
  register = false;

  constructor(private service: DirecaoService, private builder: FormBuilder) { }

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
        placa: [null, [Validators.required, Validators.maxLength(10)]],
        cpf: [null, [Validators.required, Validators.maxLength(14), Validators.minLength(14)]],
        inicioDirecao: [null, [Validators.required, Validators.maxLength(10)]]
      }
    );
  }

  // resetar formulario
  setFormulario(direcao: Direcao) {
    if (this.formulario != null) {
      this.formulario.reset();
    }

    this.formulario.patchValue(direcao);
  }

  salvar() {
    const direcao = this.formulario.value as Direcao;

    this.service.criar(direcao).subscribe(
      retorno => {
        console.dir(retorno, { depth: null });
        this.msgPositiva = true;
        this.rstFormulario();
      },
      error => {
        this.msgNegativa = error.error[0].mensagemUsuario;
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
    const direcao = this.formulario.value as Direcao;

    this.service.atualizar(direcao.codigo, direcao).subscribe(
      retorno => {
        // console.dir(retorno, { depth: null });
        this.msgPositiva = true;
        this.setFormulario(retorno);
      },
      error => {
        this.msgNegativa = error.error[0].mensagemUsuario;
      }
    );
  }

  cancelar() {
    this.rstFormulario();
  }

  excluir() {
    const direcao = this.formulario.value as Direcao;

    this.service.remover(direcao.codigo).subscribe(
      retorno => {
        this.remover = false;
        this.showList();
      }
    );
  }

  relatorio(data1: string, data2: string) {
    this.service.buscarDirecoes(data1, data2).subscribe(
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
    this.msgPositiva = false;
    this.msgNegativa = null;
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

  update(direcao: Direcao) {
    this.setFormulario(direcao);

    this.list = false;
    this.report = false;
    this.register = true;

    this.msgPositiva = false;
    this.msgNegativa = null;
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

  buscarPlaca(placa: string) {
    this.service.buscarPlaca(placa).subscribe(
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
