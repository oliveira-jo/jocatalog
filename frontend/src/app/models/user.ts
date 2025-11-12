import { role } from "./role";

export interface user {
  id?: string | null,
  //first_name: string | null,
  firstName: string | null,
  //last_name: string | null,
  lastName: string | null,
  email: string | null,
  password: string | null,
  roles: role[] | null
}

