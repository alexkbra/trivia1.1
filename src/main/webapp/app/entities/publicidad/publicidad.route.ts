import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PublicidadComponent } from './publicidad.component';
import { PublicidadDetailComponent } from './publicidad-detail.component';
import { PublicidadPopupComponent } from './publicidad-dialog.component';
import { PublicidadDeletePopupComponent } from './publicidad-delete-dialog.component';

@Injectable()
export class PublicidadResolvePagingParams implements Resolve<any> {

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

export const publicidadRoute: Routes = [
    {
        path: 'publicidad',
        component: PublicidadComponent,
        resolve: {
            'pagingParams': PublicidadResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.publicidad.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'publicidad/:id',
        component: PublicidadDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.publicidad.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const publicidadPopupRoute: Routes = [
    {
        path: 'publicidad-new',
        component: PublicidadPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.publicidad.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'publicidad/:id/edit',
        component: PublicidadPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.publicidad.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'publicidad/:id/delete',
        component: PublicidadDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.publicidad.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
