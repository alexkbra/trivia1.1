import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TriviaSharedModule } from '../../shared';
import {
    PistaService,
    PistaPopupService,
    PistaComponent,
    PistaDetailComponent,
    PistaDialogComponent,
    PistaPopupComponent,
    PistaDeletePopupComponent,
    PistaDeleteDialogComponent,
    pistaRoute,
    pistaPopupRoute,
    PistaResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...pistaRoute,
    ...pistaPopupRoute,
];

@NgModule({
    imports: [
        TriviaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PistaComponent,
        PistaDetailComponent,
        PistaDialogComponent,
        PistaDeleteDialogComponent,
        PistaPopupComponent,
        PistaDeletePopupComponent,
    ],
    entryComponents: [
        PistaComponent,
        PistaDialogComponent,
        PistaPopupComponent,
        PistaDeleteDialogComponent,
        PistaDeletePopupComponent,
    ],
    providers: [
        PistaService,
        PistaPopupService,
        PistaResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TriviaPistaModule {}
