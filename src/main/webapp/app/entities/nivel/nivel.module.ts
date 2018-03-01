import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TriviaSharedModule } from '../../shared';
import {
    NivelService,
    NivelPopupService,
    NivelComponent,
    NivelDetailComponent,
    NivelDialogComponent,
    NivelPopupComponent,
    NivelDeletePopupComponent,
    NivelDeleteDialogComponent,
    nivelRoute,
    nivelPopupRoute,
    NivelResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...nivelRoute,
    ...nivelPopupRoute,
];

@NgModule({
    imports: [
        TriviaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        NivelComponent,
        NivelDetailComponent,
        NivelDialogComponent,
        NivelDeleteDialogComponent,
        NivelPopupComponent,
        NivelDeletePopupComponent,
    ],
    entryComponents: [
        NivelComponent,
        NivelDialogComponent,
        NivelPopupComponent,
        NivelDeleteDialogComponent,
        NivelDeletePopupComponent,
    ],
    providers: [
        NivelService,
        NivelPopupService,
        NivelResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TriviaNivelModule {}
