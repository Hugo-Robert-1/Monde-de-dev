import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SubjectsListComponent } from 'src/app/pages/subjects-list/subjects-list.component';


const routes: Routes = [
  { path: '', title: 'Subjects', component: SubjectsListComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SubjectsRoutingModule {
}
