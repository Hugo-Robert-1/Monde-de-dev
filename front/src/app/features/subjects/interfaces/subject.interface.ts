export interface Subject {
  id: number;
  name: string;
  description: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface SubjectLightDTO {
  id: number;
  name: string;
}