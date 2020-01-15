// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  //urlServeurBase: 'http://213.136.77.118:8888/ampapi/api/map',
  //urlServeurBase: 'http://localhost:8080/api/map',
  //urlServeurSocketBase: 'http://localhost:8080/socket'
  //urlServeurSocketBase: 'http://213.136.77.118:8888/ampapi/socket'
  urlServeurSocketBase: 'http://213.136.77.118:8888/amp/socket',


  urlServeurBase: 'http://213.136.77.118:7001/amp/api/map',

};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
