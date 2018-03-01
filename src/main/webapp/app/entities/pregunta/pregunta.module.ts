import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TriviaSharedModule } from '../../shared';
import {
    PreguntaService,
    PreguntaPopupService,
    PreguntaComponent,
    PreguntaDetailComponent,
    PreguntaDialogComponent,
    PreguntaPopupComponent,
    PreguntaDeletePopupComponent,
    PreguntaDeleteDialogComponent,
    preguntaRoute,
    preguntaPopupRoute,
    PreguntaResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...preguntaRoute,
    ...preguntaPopupRoute,
];

@NgModule({
    imports: [
        TriviaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PreguntaComponent,
        PreguntaDetailComponent,
        PreguntaDialogComponent,
        PreguntaDeleteDialogComponent,
        PreguntaPopupComponent,
        PreguntaDeletePopupComponent,
    ],
    entryComponents: [
        PreguntaComponent,
        PreguntaDialogComponent,
        PreguntaPopupComponent,
        PreguntaDeleteDialogComponent,
        PreguntaDeletePopupComponent,
    ],
    providers: [
        PreguntaService,
        PreguntaPopupService,
        PreguntaResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TriviaPreguntaModule {}
