import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TipoPreguntaComponent } from './tipo-pregunta.component';
import { TipoPreguntaDetailComponent } from './tipo-pregunta-detail.component';
import { TipoPreguntaPopupComponent } from './tipo-pregunta-dialog.component';
import { TipoPreguntaDeletePopupComponent } from './tipo-pregunta-delete-dialog.component';

@Injectable()
export class TipoPreguntaResolvePagingParams implements Resolve<any> {

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

export const tipoPreguntaRoute: Routes = [
    {
        path: 'tipo-pregunta',
        component: TipoPreguntaComponent,
        resolve: {
            'pagingParams': TipoPreguntaResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.tipoPregunta.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tipo-pregunta/:id',
        component: TipoPreguntaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.tipoPregunta.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tipoPreguntaPopupRoute: Routes = [
    {
        path: 'tipo-pregunta-new',
        component: TipoPreguntaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.tipoPregunta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tipo-pregunta/:id/edit',
        component: TipoPreguntaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.tipoPregunta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tipo-pregunta/:id/delete',
        component: TipoPreguntaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.tipoPregunta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
