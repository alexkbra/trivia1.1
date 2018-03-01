import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TriviaSharedModule } from '../../shared';
import {
    EmpresasService,
    EmpresasPopupService,
    EmpresasComponent,
    EmpresasDetailComponent,
    EmpresasDialogComponent,
    EmpresasPopupComponent,
    EmpresasDeletePopupComponent,
    EmpresasDeleteDialogComponent,
    empresasRoute,
    empresasPopupRoute,
    EmpresasResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...empresasRoute,
    ...empresasPopupRoute,
];

@NgModule({
    imports: [
        TriviaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        EmpresasComponent,
        EmpresasDetailComponent,
        EmpresasDialogComponent,
        EmpresasDeleteDialogComponent,
        EmpresasPopupComponent,
        EmpresasDeletePopupComponent,
    ],
    entryComponents: [
        EmpresasComponent,
        EmpresasDialogComponent,
        EmpresasPopupComponent,
        EmpresasDeleteDialogComponent,
        EmpresasDeletePopupComponent,
    ],
    providers: [
        EmpresasService,
        EmpresasPopupService,
        EmpresasResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TriviaEmpresasModule {}
