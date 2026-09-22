import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { Dashboard } from './features/pos/dashboard/dashboard';
import { MainLayout } from './layout/main-layout/main-layout';

export const routes: Routes = [
    {path: 'login', component: Login},
    {
        path: '',
        component: MainLayout,
        children:[
            {path: 'dashboard', component: Dashboard},
            {path:'', redirectTo: 'dashboard', pathMatch: 'full'}
        ]
    },
    {path: '**', redirectTo: 'login'}
];
