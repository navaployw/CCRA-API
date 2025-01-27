import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { WelcomeComponent } from './pages/welcome/welcome.component';
const routes: Routes = [
  
  {
    path: 'welcome',loadChildren: () => import('./pages/welcome/welcome.module').then(m => m.WelcomeModule)
  }
];
/*
const routes: Routes = [
  {path : 'welcome', component : WelcomeComponent},
]
*/
@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes, {
        initialNavigation: 'enabled'
    }),
    CommonModule
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
