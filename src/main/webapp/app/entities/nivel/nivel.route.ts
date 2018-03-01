import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { NivelComponent } from './nivel.component';
import { NivelDetailComponent } from './nivel-detail.component';
import { NivelPopupComponent } from './nivel-dialog.component';
import { NivelDeletePopupComponent } from './nivel-delete-dialog.component';

@Injectable()
export class NivelResolvePagingParams implements Resolve<any> {

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

export const nivelRoute: Routes = [
    {
        path: 'nivel',
        component: NivelComponent,
        resolve: {
            'pagingParams': NivelResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.nivel.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'nivel/:id',
        component: NivelDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.nivel.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const nivelPopupRoute: Routes = [
    {
        path: 'nivel-new',
        component: NivelPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.nivel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'nivel/:id/edit',
        component: NivelPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.nivel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'nivel/:id/delete',
        component: NivelDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.nivel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
