import { SubjectLightDTO } from "../../subjects/interfaces/subject.interface";

export interface UserLightDTO {
  id: number;
  username: string;
}

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