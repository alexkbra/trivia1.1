import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TriviaSharedModule } from '../../shared';
import {
    RespuestaService,
    RespuestaPopupService,
    RespuestaComponent,
    RespuestaDetailComponent,
    RespuestaDialogComponent,
    RespuestaPopupComponent,
    RespuestaDeletePopupComponent,
    RespuestaDeleteDialogComponent,
    respuestaRoute,
    respuestaPopupRoute,
    RespuestaResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...respuestaRoute,
    ...respuestaPopupRoute,
];

@NgModule({
    imports: [
        TriviaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RespuestaComponent,
        RespuestaDetailComponent,
        RespuestaDialogComponent,
        RespuestaDeleteDialogComponent,
        RespuestaPopupComponent,
        RespuestaDeletePopupComponent,
    ],
    entryComponents: [
        RespuestaComponent,
        RespuestaDialogComponent,
        RespuestaPopupComponent,
        RespuestaDeleteDialogComponent,
        RespuestaDeletePopupComponent,
    ],
    providers: [
        RespuestaService,
        RespuestaPopupService,
        RespuestaResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TriviaRespuestaModule {}
