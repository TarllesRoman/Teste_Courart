import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Direcao } from '../model/direcao';

@Injectable({
  providedIn: 'root'
})
export class DirecaoService {
  private endpoint = 'direcoes/';

  constructor(private httpClient: HttpClient) { }

  criar(direcao: Direcao): Observable<Direcao> {
      return this.httpClient.post<Direcao>(`${environment.server}${this.endpoint}`, direcao);
  }

  listar(): Observable<Direcao[]> {
    return this.httpClient.get<Direcao[]>(`${environment.server}${this.endpoint}`);
  }

  buscarCodigo(codigo: number): Observable<Direcao> {
    return this.httpClient.get<Direcao>(`${environment.server}${this.endpoint}${codigo}`);
  }

  buscarCPF(cpf: string): Observable<Direcao[]> {
    return this.httpClient.get<Direcao[]>(`${environment.server}${this.endpoint}cpf/${cpf}`);
  }

  buscarPlaca(placa: string): Observable<Direcao[]> {
    return this.httpClient.get<Direcao[]>(`${environment.server}${this.endpoint}placa/${placa}`);
  }

  atualizar(codigo: number, direcao: Direcao): Observable<Direcao> {
    return this.httpClient.put<Direcao>(`${environment.server}${this.endpoint}${codigo}`, direcao);
  }

  remover(codigo: number): Observable<Direcao> {
    return this.httpClient.delete<Direcao>(`${environment.server}${this.endpoint}${codigo}`);
  }

  buscarDirecoes(data1: string, data2: string): Observable<Blob> {
    return this.httpClient.get<Blob>(`${environment.server}${this.endpoint}relatorio/${data1}/${data2}`, {
      responseType: 'blob' as 'json'
    });
  }
}
