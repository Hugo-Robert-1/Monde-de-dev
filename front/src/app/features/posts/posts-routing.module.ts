import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PostCreateComponent } from 'src/app/pages/post-create/post-create.component';
import { PostComponent } from 'src/app/pages/posts-list/post.component';


const routes: Routes = [
  { path: '', title: 'Posts', component: PostComponent },
  { path: 'create', title: 'Posts - create', component: PostCreateComponent },
//   { path: 'detail/:id', title: 'Posts - detail', component: DetailComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PostsRoutingModule {
}
