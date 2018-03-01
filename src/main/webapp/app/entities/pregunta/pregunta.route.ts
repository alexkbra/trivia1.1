import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PreguntaComponent } from './pregunta.component';
import { PreguntaDetailComponent } from './pregunta-detail.component';
import { PreguntaPopupComponent } from './pregunta-dialog.component';
import { PreguntaDeletePopupComponent } from './pregunta-delete-dialog.component';

@Injectable()
export class PreguntaResolvePagingParams implements Resolve<any> {

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

export const preguntaRoute: Routes = [
    {
        path: 'pregunta',
        component: PreguntaComponent,
        resolve: {
            'pagingParams': PreguntaResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.pregunta.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pregunta/:id',
        component: PreguntaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.pregunta.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const preguntaPopupRoute: Routes = [
    {
        path: 'pregunta-new',
        component: PreguntaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.pregunta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pregunta/:id/edit',
        component: PreguntaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.pregunta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pregunta/:id/delete',
        component: PreguntaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.pregunta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
