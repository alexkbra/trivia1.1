import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Regla } from './regla.model';
import { ReglaPopupService } from './regla-popup.service';
import { ReglaService } from './regla.service';
import { Cuestionario, CuestionarioService } from '../cuestionario';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-regla-dialog',
    templateUrl: './regla-dialog.component.html'
})
export class ReglaDialogComponent implements OnInit {

    regla: Regla;
    isSaving: boolean;

    cuestionarios: Cuestionario[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private reglaService: ReglaService,
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
        if (this.regla.id !== undefined) {
            this.subscribeToSaveResponse(
                this.reglaService.update(this.regla));
        } else {
            this.subscribeToSaveResponse(
                this.reglaService.create(this.regla));
        }
    }

    private subscribeToSaveResponse(result: Observable<Regla>) {
        result.subscribe((res: Regla) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Regla) {
        this.eventManager.broadcast({ name: 'reglaListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-regla-popup',
    template: ''
})
export class ReglaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reglaPopupService: ReglaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.reglaPopupService
                    .open(ReglaDialogComponent as Component, params['id']);
            } else {
                this.reglaPopupService
                    .open(ReglaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
