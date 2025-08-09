import { SubjectLightDTO } from "../../subjects/interfaces/subject.interface";
import { Comment } from "../../comments/interfaces/comment.interface";
import { UserLightDTO } from "../../user/interfaces/user.interface";

export interface Post {
  id: number;
  title: string;
  content: string;
  subject: SubjectLightDTO;
  createdAt: Date;
  author: UserLightDTO;
}

export interface PostCreate {
  title: string;
  content : string;
  subjectId: number;
} 

export interface PostDetailWithComments {
  id: number;
  title: string;
  content: string;
  subject: SubjectLightDTO;
  createdAt: Date;
  author: UserLightDTO;
  commentaires: Comment[],
}

export { UserLightDTO };
