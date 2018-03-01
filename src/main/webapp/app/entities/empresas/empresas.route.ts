import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { EmpresasComponent } from './empresas.component';
import { EmpresasDetailComponent } from './empresas-detail.component';
import { EmpresasPopupComponent } from './empresas-dialog.component';
import { EmpresasDeletePopupComponent } from './empresas-delete-dialog.component';

@Injectable()
export class EmpresasResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const empresasRoute: Routes = [
    {
        path: 'empresas',
        component: EmpresasComponent,
        resolve: {
            'pagingParams': EmpresasResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.empresas.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'empresas/:id',
        component: EmpresasDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.empresas.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const empresasPopupRoute: Routes = [
    {
        path: 'empresas-new',
        component: EmpresasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.empresas.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'empresas/:id/edit',
        component: EmpresasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.empresas.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'empresas/:id/delete',
        component: EmpresasDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.empresas.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
