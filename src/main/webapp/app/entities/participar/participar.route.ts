import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ParticiparComponent } from './participar.component';
import { ParticiparDetailComponent } from './participar-detail.component';
import { ParticiparPopupComponent } from './participar-dialog.component';
import { ParticiparDeletePopupComponent } from './participar-delete-dialog.component';

@Injectable()
export class ParticiparResolvePagingParams implements Resolve<any> {

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

export const participarRoute: Routes = [
    {
        path: 'participar',
        component: ParticiparComponent,
        resolve: {
            'pagingParams': ParticiparResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.participar.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'participar/:id',
        component: ParticiparDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.participar.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const participarPopupRoute: Routes = [
    {
        path: 'participar-new',
        component: ParticiparPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.participar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'participar/:id/edit',
        component: ParticiparPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.participar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'participar/:id/delete',
        component: ParticiparDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'triviaApp.participar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
