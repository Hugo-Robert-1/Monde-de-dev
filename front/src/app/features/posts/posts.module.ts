import { NgModule } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { PostsRoutingModule } from './posts-routing.module';
import localeFr from '@angular/common/locales/fr';
import { PostComponent } from 'src/app/pages/posts-list/post.component';
import { PostCardComponent } from 'src/app/components/post-card/post-card.component';
import { PostCreateComponent } from 'src/app/pages/post-create/post-create.component';
import { PostDetailsComponent } from 'src/app/pages/post-details/post-details.component';
registerLocaleData(localeFr);

const materialModules = [
  MatButtonModule,
  MatCardModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatSnackBarModule,
  MatSelectModule
];

@NgModule({
  declarations: [
    PostComponent,
    PostCreateComponent,
    PostDetailsComponent
  ],
  imports: [
    CommonModule,
    FlexLayoutModule,
    ReactiveFormsModule,
    PostsRoutingModule,
    ...materialModules,
    PostCardComponent,
    FormsModule
  ]
})
export class PostsModule { }
