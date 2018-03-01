import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Nivel } from './nivel.model';
import { NivelPopupService } from './nivel-popup.service';
import { NivelService } from './nivel.service';
import { Cuestionario, CuestionarioService } from '../cuestionario';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-nivel-dialog',
    templateUrl: './nivel-dialog.component.html'
})
export class NivelDialogComponent implements OnInit {

    nivel: Nivel;
    isSaving: boolean;

    cuestionarios: Cuestionario[];
    fechaInicioDp: any;
    fechaFinalDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private nivelService: NivelService,
        private cuestionarioService: CuestionarioService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.cuestionarioService.query()
            .subscribe((res: ResponseWrapper) => { this.cuestionarios = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.nivel.id !== undefined) {
            this.subscribeToSaveResponse(
                this.nivelService.update(this.nivel));
        } else {
            this.subscribeToSaveResponse(
                this.nivelService.create(this.nivel));
        }
    }

    private subscribeToSaveResponse(result: Observable<Nivel>) {
        result.subscribe((res: Nivel) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Nivel) {
        this.eventManager.broadcast({ name: 'nivelListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackCuestionarioById(index: number, item: Cuestionario) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-nivel-popup',
    template: ''
})
export class NivelPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private nivelPopupService: NivelPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.nivelPopupService
                    .open(NivelDialogComponent as Component, params['id']);
            } else {
                this.nivelPopupService
                    .open(NivelDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
