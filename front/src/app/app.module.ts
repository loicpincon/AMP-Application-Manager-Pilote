import { NgModule, APP_INITIALIZER } from '@angular/core';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { apiMapLoaderConfigFactory, ApiMapLoaderConfig } from './core/services/apiMapLoaderConfig.config';
import { LoaderService } from './core/services/loader.service';
import { MAT_DIALOG_DEFAULT_OPTIONS } from '@angular/material/dialog';
import { SidenavService } from './core/services/sideNav.service';
import { CoreModule } from './core/core.module';
import { AuthentifieModule } from './authentifie/authentifie.module';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: 'secure', loadChildren: './authentifie/authentifie.module#AuthentifieModule'
  },
  {
    path: 'unsecure', loadChildren: './public/public.module#PublicModule'
  },
  {
    path: '**', redirectTo: 'secure'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: false })],
  exports: [RouterModule]
})
export class AppRoutingModule { }

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    CoreModule, AuthentifieModule],
  providers: [
    { provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { hasBackdrop: false } },
    LoaderService,
    SidenavService,
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
