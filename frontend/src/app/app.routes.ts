import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { Dashboard } from './features/pos/dashboard/dashboard';
import { App } from './app';

export const routes: Routes = [
    {path: 'login', component: Login},
    {
        path: '',
        component: App,
        children:[
            {path: 'dashboard', component: Dashboard},
            {path:'', redirectTo: 'dashboard', pathMatch: 'full'}
        ]
    },
    {path: '**', redirectTo: 'login'}
];
