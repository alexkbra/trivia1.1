import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TriviaSharedModule } from '../../shared';
import {
    ParticiparService,
    ParticiparPopupService,
    ParticiparComponent,
    ParticiparDetailComponent,
    ParticiparDialogComponent,
    ParticiparPopupComponent,
    ParticiparDeletePopupComponent,
    ParticiparDeleteDialogComponent,
    participarRoute,
    participarPopupRoute,
    ParticiparResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...participarRoute,
    ...participarPopupRoute,
];

@NgModule({
    imports: [
        TriviaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ParticiparComponent,
        ParticiparDetailComponent,
        ParticiparDialogComponent,
        ParticiparDeleteDialogComponent,
        ParticiparPopupComponent,
        ParticiparDeletePopupComponent,
    ],
    entryComponents: [
        ParticiparComponent,
        ParticiparDialogComponent,
        ParticiparPopupComponent,
        ParticiparDeleteDialogComponent,
        ParticiparDeletePopupComponent,
    ],
    providers: [
        ParticiparService,
        ParticiparPopupService,
        ParticiparResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TriviaParticiparModule {}
