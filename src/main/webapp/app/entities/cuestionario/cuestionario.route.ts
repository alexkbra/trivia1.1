import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CuestionarioComponent } from './cuestionario.component';
import { CuestionarioDetailComponent } from './cuestionario-detail.component';
import { CuestionarioPopupComponent } from './cuestionario-dialog.component';
import { CuestionarioDeletePopupComponent } from './cuestionario-delete-dialog.component';

@Injectable()
export class CuestionarioResolvePagingParams implements Resolve<any> {

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

export const cuestionarioRoute: Routes = [
    {
        path: 'cuestionario',
        component: CuestionarioComponent,
        resolve: {
            'pagingParams': CuestionarioResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.cuestionario.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cuestionario/:id',
        component: CuestionarioDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.cuestionario.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cuestionarioPopupRoute: Routes = [
    {
        path: 'cuestionario-new',
        component: CuestionarioPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.cuestionario.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cuestionario/:id/edit',
        component: CuestionarioPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.cuestionario.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cuestionario/:id/delete',
        component: CuestionarioDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.cuestionario.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
