import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TriviaSharedModule } from '../../shared';
import {
    PublicidadService,
    PublicidadPopupService,
    PublicidadComponent,
    PublicidadDetailComponent,
    PublicidadDialogComponent,
    PublicidadPopupComponent,
    PublicidadDeletePopupComponent,
    PublicidadDeleteDialogComponent,
    publicidadRoute,
    publicidadPopupRoute,
    PublicidadResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...publicidadRoute,
    ...publicidadPopupRoute,
];

@NgModule({
    imports: [
        TriviaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PublicidadComponent,
        PublicidadDetailComponent,
        PublicidadDialogComponent,
        PublicidadDeleteDialogComponent,
        PublicidadPopupComponent,
        PublicidadDeletePopupComponent,
    ],
    entryComponents: [
        PublicidadComponent,
        PublicidadDialogComponent,
        PublicidadPopupComponent,
        PublicidadDeleteDialogComponent,
        PublicidadDeletePopupComponent,
    ],
    providers: [
        PublicidadService,
        PublicidadPopupService,
        PublicidadResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TriviaPublicidadModule {}
