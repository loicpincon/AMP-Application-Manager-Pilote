import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../material.module';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { GeneralHeaderInterceptor } from './interceptor/GeneralHeaderInterceptor';
import { ErrorComponent } from './error/error.component';
import { Routes, RouterModule } from '@angular/router';
import { AuthentComponent } from '../public/authent/authent.component';
import { CoreComponent } from './core.component';
import { ToastrModule, ToastrService } from 'ngx-toastr';


const routes: Routes = [
  {
    path: 'error', component: ErrorComponent,
  },
  {
    path: '**', redirectTo: 'error'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CoreRoutingModule { }


@NgModule({
  declarations: [
    ErrorComponent,
    CoreComponent],
  imports: [
    CommonModule,
    CoreRoutingModule,
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
