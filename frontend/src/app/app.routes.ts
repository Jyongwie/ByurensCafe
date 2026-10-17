import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { Dashboard } from './features/pos/dashboard/dashboard';
import { MainLayout } from './layout/main-layout/main-layout';
import { Order } from './features/pos/order/order';
import { Venue } from './features/pos/venue/venue';

export const routes: Routes = [
    {path: 'login', component: Login},
    {
        path: '',
        component: MainLayout,
        children:[
            {path: 'dashboard', component: Dashboard},
            {path: 'orders', component: Order},
            {path: 'venue', component: Venue},
            {path:'', redirectTo: 'dashboard', pathMatch: 'full'}
        ]
    },
    {path: '**', redirectTo: 'login'}
];
