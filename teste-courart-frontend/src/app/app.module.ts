import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MotoristaComponent } from './motorista/motorista.component';
import { NavbarComponent } from './navbar/navbar.component';
import {NgxMaskModule} from 'ngx-mask';
import { VeiculoComponent } from './veiculo/veiculo.component';
import { DirecaoComponent } from './direcao/direcao.component';

@NgModule({
  declarations: [
    AppComponent,
    MotoristaComponent,
    NavbarComponent,
    VeiculoComponent,
    DirecaoComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgxMaskModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
