import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PistaComponent } from './pista.component';
import { PistaDetailComponent } from './pista-detail.component';
import { PistaPopupComponent } from './pista-dialog.component';
import { PistaDeletePopupComponent } from './pista-delete-dialog.component';

@Injectable()
export class PistaResolvePagingParams implements Resolve<any> {

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

export const pistaRoute: Routes = [
    {
        path: 'pista',
        component: PistaComponent,
        resolve: {
            'pagingParams': PistaResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.pista.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pista/:id',
        component: PistaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.pista.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pistaPopupRoute: Routes = [
    {
        path: 'pista-new',
        component: PistaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.pista.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pista/:id/edit',
        component: PistaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.pista.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pista/:id/delete',
        component: PistaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.pista.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
