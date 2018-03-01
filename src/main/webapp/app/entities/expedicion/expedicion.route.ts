import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ExpedicionComponent } from './expedicion.component';
import { ExpedicionDetailComponent } from './expedicion-detail.component';
import { ExpedicionPopupComponent } from './expedicion-dialog.component';
import { ExpedicionDeletePopupComponent } from './expedicion-delete-dialog.component';

@Injectable()
export class ExpedicionResolvePagingParams implements Resolve<any> {

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

export const expedicionRoute: Routes = [
    {
        path: 'expedicion',
        component: ExpedicionComponent,
        resolve: {
            'pagingParams': ExpedicionResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.expedicion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'expedicion/:id',
        component: ExpedicionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.expedicion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const expedicionPopupRoute: Routes = [
    {
        path: 'expedicion-new',
        component: ExpedicionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.expedicion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'expedicion/:id/edit',
        component: ExpedicionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.expedicion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'expedicion/:id/delete',
        component: ExpedicionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.expedicion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
