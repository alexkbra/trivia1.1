import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TriviaSharedModule } from '../../shared';
import {
    CuestionarioService,
    CuestionarioPopupService,
    CuestionarioComponent,
    CuestionarioDetailComponent,
    CuestionarioDialogComponent,
    CuestionarioPopupComponent,
    CuestionarioDeletePopupComponent,
    CuestionarioDeleteDialogComponent,
    cuestionarioRoute,
    cuestionarioPopupRoute,
    CuestionarioResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...cuestionarioRoute,
    ...cuestionarioPopupRoute,
];

@NgModule({
    imports: [
        TriviaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CuestionarioComponent,
        CuestionarioDetailComponent,
        CuestionarioDialogComponent,
        CuestionarioDeleteDialogComponent,
        CuestionarioPopupComponent,
        CuestionarioDeletePopupComponent,
    ],
    entryComponents: [
        CuestionarioComponent,
        CuestionarioDialogComponent,
        CuestionarioPopupComponent,
        CuestionarioDeleteDialogComponent,
        CuestionarioDeletePopupComponent,
    ],
    providers: [
        CuestionarioService,
        CuestionarioPopupService,
        CuestionarioResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TriviaCuestionarioModule {}
