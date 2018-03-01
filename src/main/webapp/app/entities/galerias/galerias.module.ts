import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TriviaSharedModule } from '../../shared';
import {
    GaleriasService,
    GaleriasPopupService,
    GaleriasComponent,
    GaleriasDetailComponent,
    GaleriasDialogComponent,
    GaleriasPopupComponent,
    GaleriasDeletePopupComponent,
    GaleriasDeleteDialogComponent,
    galeriasRoute,
    galeriasPopupRoute,
    GaleriasResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...galeriasRoute,
    ...galeriasPopupRoute,
];

@NgModule({
    imports: [
        TriviaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        GaleriasComponent,
        GaleriasDetailComponent,
        GaleriasDialogComponent,
        GaleriasDeleteDialogComponent,
        GaleriasPopupComponent,
        GaleriasDeletePopupComponent,
    ],
    entryComponents: [
        GaleriasComponent,
        GaleriasDialogComponent,
        GaleriasPopupComponent,
        GaleriasDeleteDialogComponent,
        GaleriasDeletePopupComponent,
    ],
    providers: [
        GaleriasService,
        GaleriasPopupService,
        GaleriasResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TriviaGaleriasModule {}
