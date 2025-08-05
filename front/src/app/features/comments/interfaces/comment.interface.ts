import { UserLightDTO } from "../../posts/interfaces/post.interface";

export interface Comment {
  id: number;
  content: string;
  createdAt: Date;
  user: UserLightDTO;
}

export interface CommentCreate {
  content: string;
  postId: number;
}