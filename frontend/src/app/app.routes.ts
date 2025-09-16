import { Routes } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/user/login/login.component';
import { RegisterComponent } from './components/user/register/register.component';
import { authUserGuard } from './services/guards/auth-user.guard';

import { ProductCreateComponent } from './components/produc/private/product-create/product-create.component';
import { ProductUpdateComponent } from './components/produc/private/product-update/product-update.component';
import { ProductDetailsComponent } from './components/produc/public/product-details/product-details.component';

import { ProductListAdminComponent } from './components/produc/private/product-list-admin/product-list-admin.component';
import { ProductListComponent } from './components/produc/public/product-list/product-list.component';
import { SearchComponent } from './components/produc/public/search/search.component';

import { CategoryListComponent } from './components/category/category-list/category-list.component';

export const routes: Routes = [

  // Non Auth User - canActivate: [nonAuthUserGuard]
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'products', component: ProductListComponent },
  { path: 'products/search', component: SearchComponent },
  { path: 'products/:id/details', component: ProductDetailsComponent },
  { path: 'categories', component: CategoryListComponent },

  // Just for User Auth
  { path: 'products/create', component: ProductCreateComponent, canActivate: [authUserGuard] },
  { path: 'products/:id/update', component: ProductUpdateComponent, canActivate: [authUserGuard] },
  { path: 'products-painel', component: ProductListAdminComponent, canActivate: [authUserGuard] },

];
