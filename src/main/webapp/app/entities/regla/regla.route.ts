import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ReglaComponent } from './regla.component';
import { ReglaDetailComponent } from './regla-detail.component';
import { ReglaPopupComponent } from './regla-dialog.component';
import { ReglaDeletePopupComponent } from './regla-delete-dialog.component';

@Injectable()
export class ReglaResolvePagingParams implements Resolve<any> {

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

export const reglaRoute: Routes = [
    {
        path: 'regla',
        component: ReglaComponent,
        resolve: {
            'pagingParams': ReglaResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.regla.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'regla/:id',
        component: ReglaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.regla.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reglaPopupRoute: Routes = [
    {
        path: 'regla-new',
        component: ReglaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.regla.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'regla/:id/edit',
        component: ReglaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.regla.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'regla/:id/delete',
        component: ReglaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.regla.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
