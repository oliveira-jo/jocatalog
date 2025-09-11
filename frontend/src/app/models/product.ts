import { category } from "./category";

export interface product {
  id?: string | null,
  name: string | null,
  description: string | null,
  price: string | null,
  imgUrl: string | null,
  categories: Array<category>
}