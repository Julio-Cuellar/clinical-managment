export interface UserDTO {
  id: number;
  name: string;
  lastName: string;
  email: string;
  username: string;
  phoneNumber?: string;
  bornDate?: string;
  role: string;
  enabled: boolean;
  consultorioIds?: number[];
}