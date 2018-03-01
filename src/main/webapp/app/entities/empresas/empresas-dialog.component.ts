import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Empresas } from './empresas.model';
import { EmpresasPopupService } from './empresas-popup.service';
import { EmpresasService } from './empresas.service';

@Component({
    selector: 'jhi-empresas-dialog',
    templateUrl: './empresas-dialog.component.html'
})
export class EmpresasDialogComponent implements OnInit {

    empresas: Empresas;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private empresasService: EmpresasService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.empresas.id !== undefined) {
            this.subscribeToSaveResponse(
                this.empresasService.update(this.empresas));
        } else {
            this.subscribeToSaveResponse(
                this.empresasService.create(this.empresas));
        }
    }

    private subscribeToSaveResponse(result: Observable<Empresas>) {
        result.subscribe((res: Empresas) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Empresas) {
        this.eventManager.broadcast({ name: 'empresasListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-empresas-popup',
    template: ''
})
export class EmpresasPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private empresasPopupService: EmpresasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.empresasPopupService
                    .open(EmpresasDialogComponent as Component, params['id']);
            } else {
                this.empresasPopupService
                    .open(EmpresasDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
