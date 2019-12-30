import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from './core/core.module';
import { ApplicationModule } from './application/application.module';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { apiMapLoaderConfigFactory, ApiMapLoaderConfig } from './core/services/apiMapLoaderConfig.config';
import { LoaderService } from './core/services/loader.service';
import { ApmHeaderInterceptor } from './core/services/apmHeaderInterceptor';
import { CommonModule } from '@angular/common';
import { MatButtonModule, MatMenuModule, MatToolbarModule, MatSidenavModule, MatExpansionModule } from '@angular/material';
import { ApplicationRoutingModule } from './application/applicationRouting.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    CoreModule,
    HttpClientModule,
    ApplicationModule,
    CommonModule,
    MatButtonModule,
    MatMenuModule,
    MatToolbarModule,
    MatSidenavModule,
    MatExpansionModule
  ],
  providers: [
    LoaderService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ApmHeaderInterceptor,
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
