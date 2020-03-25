import { VeiculoComponent } from './veiculo/veiculo.component';
import { MotoristaComponent } from './motorista/motorista.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DirecaoComponent } from './direcao/direcao.component';


const routes: Routes = [
  {
   path: 'motoristas',
   component: MotoristaComponent
  },
  {
    path: 'veiculos',
    component: VeiculoComponent
   },
   {
    path: 'direcoes',
    component: DirecaoComponent
   },
  {
    path: '',
    redirectTo: 'motoristas',
    pathMatch: 'full'
   },
   {
    path: '**',
    redirectTo: 'motoristas',
    pathMatch: 'full'
   }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
