import { VeiculoService } from './veiculo.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Veiculo } from '../model/veiculo';

@Component({
  selector: 'app-veiculo',
  templateUrl: './veiculo.component.html',
  styleUrls: ['./veiculo.component.css']
})
export class VeiculoComponent implements OnInit {

  formulario: FormGroup;
  formRelatorio: FormGroup;
  lista: Veiculo[];
  aparecerMsg = false;

  list = true;
  report = false;
  register = false;

  remover = false;

  ativo: any;

  constructor(private service: VeiculoService, private builder: FormBuilder) { }

  ngOnInit() {
    this.rstFormulario();
    this.listar();

    this.formRelatorio = this.builder.group(
      {
        data1: [ null, [Validators.required, Validators.maxLength(10)] ],
        data2: [ null, [Validators.required, Validators.maxLength(10)] ],
      }
    );
  }

  // resetar formulario
  rstFormulario() {
    if (this.formulario != null) {
      this.formulario.reset();
    }
    this.formulario = this.builder.group(
      {
        codigo: [null],
        placa: [ null, [Validators.required, Validators.maxLength(10)] ],
        ativo: [true],
        anoFabricacao: [ null, [Validators.required, Validators.maxLength(4), Validators.minLength(4)] ],
        anoModelo: [ null, [Validators.required, Validators.maxLength(4), Validators.minLength(4)] ],
        chassi: [ null, [Validators.required, Validators.maxLength(40)] ],
        dataCadastro: [ null, [Validators.required, Validators.maxLength(10), Validators.minLength(10)] ],
        dataDesativacao: [ null, [Validators.maxLength(10)] ],
        modelo: [ null, [Validators.required, Validators.maxLength(30)] ],
        cor: [ null, [Validators.required, Validators.maxLength(20)] ],
        qtdPassageiros: [ 4, [Validators.required] ],
        consumoMedio: [ null, [Validators.required] ]
      }
    );
  }

  // resetar formulario
  setFormulario(veiculo: Veiculo) {
    if (this.formulario != null) {
      this.formulario.reset();
    }

    this.formulario.patchValue(veiculo);
    console.dir(this.formulario, { depth: null });
  }

  salvar() {
    const veiculo = this.formulario.value as Veiculo;

    if (veiculo.dataDesativacao == null) {
      veiculo.ativo = true;
    }

    this.service.criar(veiculo).subscribe(
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
    const veiculo = this.formulario.value as Veiculo;

    this.service.atualizar(veiculo.codigo, veiculo).subscribe(
      retorno => {
        // console.dir(retorno, { depth: null });
        this.aparecerMsg = true;
        this.setFormulario(retorno);
      }
    );
  }

  atualizarAtivo(ativo: boolean) {
    const veiculo = this.formulario.value as Veiculo;

    this.service.atualizarAtivo(veiculo.codigo, ativo).subscribe(
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
    const veiculo = this.formulario.value as Veiculo;

    this.service.remover(veiculo.codigo).subscribe(
      retorno => {
        this.remover = false;
        this.showList();
      }
    );
  }

  ativados(data1: string, data2: string) {
    this.service.buscarAtivados(data1, data2).subscribe(
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
    this.rstFormulario();
    this.list = false;
    this.report = false;
    this.register = true;

    this.aparecerMsg = false;
  }

  showReport() {
    this.list = false;
    this.report = true;
    this.register = false;

    if (this.formRelatorio != null) {
      this.formRelatorio.reset();
    }
  }

  update(veiculo: Veiculo) {
    this.list = false;
    this.report = false;
    this.register = true;

    this.aparecerMsg = false;
    this.setFormulario(veiculo);
  }

  buscarModelo(modelo: string) {
    this.service.buscarModelo(modelo).subscribe(
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
