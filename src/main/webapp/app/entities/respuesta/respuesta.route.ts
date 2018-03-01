import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RespuestaComponent } from './respuesta.component';
import { RespuestaDetailComponent } from './respuesta-detail.component';
import { RespuestaPopupComponent } from './respuesta-dialog.component';
import { RespuestaDeletePopupComponent } from './respuesta-delete-dialog.component';

@Injectable()
export class RespuestaResolvePagingParams implements Resolve<any> {

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

export const respuestaRoute: Routes = [
    {
        path: 'respuesta',
        component: RespuestaComponent,
        resolve: {
            'pagingParams': RespuestaResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.respuesta.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'respuesta/:id',
        component: RespuestaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.respuesta.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const respuestaPopupRoute: Routes = [
    {
        path: 'respuesta-new',
        component: RespuestaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.respuesta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'respuesta/:id/edit',
        component: RespuestaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.respuesta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'respuesta/:id/delete',
        component: RespuestaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.respuesta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
