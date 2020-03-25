import { Motorista } from './../model/motorista';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MotoristaService {

  private endpoint = 'motoristas/';

  constructor(private httpClient: HttpClient) { }

  criar(motorista: Motorista): Observable<Motorista> {
      return this.httpClient.post<Motorista>(`${environment.server}${this.endpoint}`, motorista);
  }

  listar(): Observable<Motorista[]> {
    return this.httpClient.get<Motorista[]>(`${environment.server}${this.endpoint}`);
  }

  buscarCodigo(codigo: number): Observable<Motorista> {
    return this.httpClient.get<Motorista>(`${environment.server}${this.endpoint}${codigo}`);
  }

  buscarCPF(cpf: string): Observable<Motorista[]> {
    return this.httpClient.get<Motorista[]>(`${environment.server}${this.endpoint}cpf/${cpf}`);
  }

  buscarNome(nome: string): Observable<Motorista[]> {
    return this.httpClient.get<Motorista[]>(`${environment.server}${this.endpoint}nome/${nome}`);
  }

  atualizar(codigo: number, motorista: Motorista): Observable<Motorista> {
    return this.httpClient.put<Motorista>(`${environment.server}${this.endpoint}${codigo}`, motorista);
  }

  remover(codigo: number): Observable<Motorista> {
    return this.httpClient.delete<Motorista>(`${environment.server}${this.endpoint}${codigo}`);
  }

  buscarAniversariantes(data1: string, data2: string): Observable<Blob> {
    return this.httpClient.get<Blob>(`${environment.server}${this.endpoint}aniversariantes/${data1}/${data2}`, {
      responseType: 'blob' as 'json'
    });
  }

}
