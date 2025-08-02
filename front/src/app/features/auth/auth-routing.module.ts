import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from '../../pages/login/login.component';
import { RegisterComponent } from 'src/app/pages/register/register.component';

const routes: Routes = [
  { title: 'Login', path: 'login', component: LoginComponent },
  { title: 'Login', path: 'register', component: RegisterComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
