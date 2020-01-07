import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from './core/core.module';
import { ApplicationModule } from './authentifie/application/application.module';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { apiMapLoaderConfigFactory, ApiMapLoaderConfig } from './core/services/apiMapLoaderConfig.config';
import { LoaderService } from './core/services/loader.service';
import { CommonModule } from '@angular/common';

import { MAT_DIALOG_DEFAULT_OPTIONS } from '@angular/material/dialog';
import { FormGroup, FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AddHeaderInterceptor } from './core/interceptor/HeaderInterceptor';
import { SidenavService } from './core/services/sideNav.service';
import { MaterialModule } from './material.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    CoreModule,
    HttpClientModule,
    ApplicationModule,
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule
  ],
  providers: [
    { provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { hasBackdrop: false } },

    LoaderService,
    SidenavService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AddHeaderInterceptor,
      multi: true,
    },
    {
      provide: APP_INITIALIZER,
      useFactory: apiMapLoaderConfigFactory,
      deps: [ApiMapLoaderConfig],
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
