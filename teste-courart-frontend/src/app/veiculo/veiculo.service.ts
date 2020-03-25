import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { Veiculo } from './../model/veiculo';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class VeiculoService {
  private endpoint = 'veiculos/';

  constructor(private httpClient: HttpClient) { }

  criar(veiculo: Veiculo): Observable<Veiculo> {
      return this.httpClient.post<Veiculo>(`${environment.server}${this.endpoint}`, veiculo);
  }

  listar(): Observable<Veiculo[]> {
    return this.httpClient.get<Veiculo[]>(`${environment.server}${this.endpoint}`);
  }

  buscarCodigo(codigo: number): Observable<Veiculo> {
    return this.httpClient.get<Veiculo>(`${environment.server}${this.endpoint}${codigo}`);
  }

  buscarModelo(modelo: string): Observable<Veiculo[]> {
    return this.httpClient.get<Veiculo[]>(`${environment.server}${this.endpoint}modelo/${modelo}`);
  }

  buscarPlaca(placa: string): Observable<Veiculo[]> {
    return this.httpClient.get<Veiculo[]>(`${environment.server}${this.endpoint}placa/${placa}`);
  }

  atualizar(codigo: number, veiculo: Veiculo): Observable<Veiculo> {
    return this.httpClient.put<Veiculo>(`${environment.server}${this.endpoint}${codigo}`, veiculo);
  }

  atualizarAtivo(codigo: number, ativo: boolean): Observable<Veiculo> {
    return this.httpClient.put<Veiculo>(`${environment.server}${this.endpoint}${codigo}`, ativo);
  }

  remover(codigo: number): Observable<Veiculo> {
    return this.httpClient.delete<Veiculo>(`${environment.server}${this.endpoint}${codigo}`);
  }

  buscarAtivados(data1: string, data2: string): Observable<Blob> {
    return this.httpClient.get<Blob>(`${environment.server}${this.endpoint}ativados/${data1}/${data2}`, {
      responseType: 'blob' as 'json'
    });
  }
}
