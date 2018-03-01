import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TriviaSharedModule } from '../../shared';
import {
    ReglaService,
    ReglaPopupService,
    ReglaComponent,
    ReglaDetailComponent,
    ReglaDialogComponent,
    ReglaPopupComponent,
    ReglaDeletePopupComponent,
    ReglaDeleteDialogComponent,
    reglaRoute,
    reglaPopupRoute,
    ReglaResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...reglaRoute,
    ...reglaPopupRoute,
];

@NgModule({
    imports: [
        TriviaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ReglaComponent,
        ReglaDetailComponent,
        ReglaDialogComponent,
        ReglaDeleteDialogComponent,
        ReglaPopupComponent,
        ReglaDeletePopupComponent,
    ],
    entryComponents: [
        ReglaComponent,
        ReglaDialogComponent,
        ReglaPopupComponent,
        ReglaDeleteDialogComponent,
        ReglaDeletePopupComponent,
    ],
    providers: [
        ReglaService,
        ReglaPopupService,
        ReglaResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TriviaReglaModule {}
