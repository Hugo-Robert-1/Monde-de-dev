import { Subject } from "../../subjects/interfaces/subject.interface";

export interface User {
  id: number;
  username: string;
  email: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface UserLightDTO {
  id: number;
  username: string;
}

export interface UserWithSubjects {
  id: number;
  username:  String;
  email: String;
  subscribedSubjects: Subject[]
}

export interface UserUpdated {
  username:  String;
  email: String;
  password: String;
}