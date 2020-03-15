import { NgModule, APP_INITIALIZER, Component, OnInit } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { apiMapLoaderConfigFactory, ApiMapLoaderConfig } from './core/services/apiMapLoaderConfig.config';
import { LoaderService } from './core/services/loader.service';
import { MAT_DIALOG_DEFAULT_OPTIONS } from '@angular/material/dialog';
import { SidenavService } from './core/services/sideNav.service';
import { CoreModule } from './core/core.module';
import { AuthentifieModule } from './authentifie/authentifie.module';
import { Routes, RouterModule } from '@angular/router';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { AuthGuardService as AuthGuard } from './core/services/auth-guard.service';
import { ChartsModule } from 'ng2-charts';
import { ApmService } from './core/services/apm.service';
import { ApiManagerService } from './core/services/api-manager.service';
import { ToastrModule, ToastrService } from 'ngx-toastr';

const routes: Routes = [
  {
    path: 'secure', loadChildren: './authentifie/authentifie.module#AuthentifieModule', canLoad: [AuthGuard]
  },
  {
    path: 'unsecure', loadChildren: './public/public.module#PublicModule'
  },
  {
    path: 'core', loadChildren: './core/core.module#CoreModule'
  },
  {
    path: '**', redirectTo: 'secure'
  }
];

@Component({
  selector: 'app-root',
  template: '<router-outlet></router-outlet>'
})
export class AppComponent {


}

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
    CoreModule,
    ChartsModule,
    ToastrModule.forRoot(),
    AuthentifieModule],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    { provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { hasBackdrop: false } },
    LoaderService,
    SidenavService,
    { provide: ToastrService, useClass: ToastrService }
    ,
    {
      provide: APP_INITIALIZER,
      useFactory: apiMapLoaderConfigFactory,
      deps: [ApiMapLoaderConfig],
      multi: true
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {




}





