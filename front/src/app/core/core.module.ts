import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../material.module';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { GeneralHeaderInterceptor } from './interceptor/GeneralHeaderInterceptor';


@NgModule({
  declarations: [
  ],
  imports: [
    CommonModule,

    ReactiveFormsModule,
    MaterialModule
  ], providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: GeneralHeaderInterceptor,
      multi: true,
    }
  ]
})
export class CoreModule { }
