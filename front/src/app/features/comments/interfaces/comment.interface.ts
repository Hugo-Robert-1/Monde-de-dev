import { UserLightDTO } from "../../user/interfaces/user.interface";

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