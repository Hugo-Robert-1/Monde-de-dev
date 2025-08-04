import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PostComponent } from 'src/app/pages/posts-list/post.component';


const routes: Routes = [
  { path: '', title: 'Posts', component: PostComponent },
//   { path: 'detail/:id', title: 'Posts - detail', component: DetailComponent },
//   { path: 'create', title: 'Posts - create', component: FormComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PostsRoutingModule {
}
