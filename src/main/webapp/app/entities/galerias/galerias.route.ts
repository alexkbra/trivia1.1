import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { GaleriasComponent } from './galerias.component';
import { GaleriasDetailComponent } from './galerias-detail.component';
import { GaleriasPopupComponent } from './galerias-dialog.component';
import { GaleriasDeletePopupComponent } from './galerias-delete-dialog.component';

@Injectable()
export class GaleriasResolvePagingParams implements Resolve<any> {

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

export const galeriasRoute: Routes = [
    {
        path: 'galerias',
        component: GaleriasComponent,
        resolve: {
            'pagingParams': GaleriasResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.galerias.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'galerias/:id',
        component: GaleriasDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.galerias.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const galeriasPopupRoute: Routes = [
    {
        path: 'galerias-new',
        component: GaleriasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.galerias.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'galerias/:id/edit',
        component: GaleriasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.galerias.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'galerias/:id/delete',
        component: GaleriasDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.galerias.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
