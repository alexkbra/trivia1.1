import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TriviaSharedModule } from '../../shared';
import {
    TipoPreguntaService,
    TipoPreguntaPopupService,
    TipoPreguntaComponent,
    TipoPreguntaDetailComponent,
    TipoPreguntaDialogComponent,
    TipoPreguntaPopupComponent,
    TipoPreguntaDeletePopupComponent,
    TipoPreguntaDeleteDialogComponent,
    tipoPreguntaRoute,
    tipoPreguntaPopupRoute,
    TipoPreguntaResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...tipoPreguntaRoute,
    ...tipoPreguntaPopupRoute,
];

@NgModule({
    imports: [
        TriviaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TipoPreguntaComponent,
        TipoPreguntaDetailComponent,
        TipoPreguntaDialogComponent,
        TipoPreguntaDeleteDialogComponent,
        TipoPreguntaPopupComponent,
        TipoPreguntaDeletePopupComponent,
    ],
    entryComponents: [
        TipoPreguntaComponent,
        TipoPreguntaDialogComponent,
        TipoPreguntaPopupComponent,
        TipoPreguntaDeleteDialogComponent,
        TipoPreguntaDeletePopupComponent,
    ],
    providers: [
        TipoPreguntaService,
        TipoPreguntaPopupService,
        TipoPreguntaResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TriviaTipoPreguntaModule {}
