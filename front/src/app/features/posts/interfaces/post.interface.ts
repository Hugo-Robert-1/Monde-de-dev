export interface SubjectLightDTO {
  id: number;
  name: string;
}

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