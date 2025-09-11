import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/user/login/login.component';
import { RegisterComponent } from './components/user/register/register.component';
import { ProductDetailsComponent } from './components/produc/product-details/product-details.component';
import { ProductCreateComponent } from './components/produc/product-create/product-create.component';
import { ProductUpdateComponent } from './components/produc/product-update/product-update.component';
import { authUserGuard } from './services/guards/auth-user.guard';
import { ProductListAdminComponent } from './components/produc/product-list-admin/product-list-admin.component';
import { ProductListComponent } from './components/produc/product-list/product-list.component';

export const routes: Routes = [

  // Non Auth User - canActivate: [nonAuthUserGuard]
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },

  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  { path: 'products', component: ProductListComponent },
  { path: 'products/:id/details', component: ProductDetailsComponent },

  // Just for User Auth
  { path: 'products/create', component: ProductCreateComponent, canActivate: [authUserGuard] },
  { path: 'products/:id/update', component: ProductUpdateComponent, canActivate: [authUserGuard] },
  { path: 'products/admin', component: ProductListAdminComponent, canActivate: [authUserGuard] },



];
