import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TriviaSharedModule } from '../../shared';
import {
    ExpedicionService,
    ExpedicionPopupService,
    ExpedicionComponent,
    ExpedicionDetailComponent,
    ExpedicionDialogComponent,
    ExpedicionPopupComponent,
    ExpedicionDeletePopupComponent,
    ExpedicionDeleteDialogComponent,
    expedicionRoute,
    expedicionPopupRoute,
    ExpedicionResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...expedicionRoute,
    ...expedicionPopupRoute,
];

@NgModule({
    imports: [
        TriviaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ExpedicionComponent,
        ExpedicionDetailComponent,
        ExpedicionDialogComponent,
        ExpedicionDeleteDialogComponent,
        ExpedicionPopupComponent,
        ExpedicionDeletePopupComponent,
    ],
    entryComponents: [
        ExpedicionComponent,
        ExpedicionDialogComponent,
        ExpedicionPopupComponent,
        ExpedicionDeleteDialogComponent,
        ExpedicionDeletePopupComponent,
    ],
    providers: [
        ExpedicionService,
        ExpedicionPopupService,
        ExpedicionResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TriviaExpedicionModule {}
