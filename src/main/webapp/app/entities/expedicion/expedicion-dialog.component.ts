import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Expedicion } from './expedicion.model';
import { ExpedicionPopupService } from './expedicion-popup.service';
import { ExpedicionService } from './expedicion.service';
import { Publicidad, PublicidadService } from '../publicidad';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-expedicion-dialog',
    templateUrl: './expedicion-dialog.component.html'
})
export class ExpedicionDialogComponent implements OnInit {

    expedicion: Expedicion;
    isSaving: boolean;

    publicidads: Publicidad[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private expedicionService: ExpedicionService,
        private publicidadService: PublicidadService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.publicidadService.query()
            .subscribe((res: ResponseWrapper) => { this.publicidads = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.expedicion.id !== undefined) {
            this.subscribeToSaveResponse(
                this.expedicionService.update(this.expedicion));
        } else {
            this.subscribeToSaveResponse(
                this.expedicionService.create(this.expedicion));
        }
    }

    private subscribeToSaveResponse(result: Observable<Expedicion>) {
        result.subscribe((res: Expedicion) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Expedicion) {
        this.eventManager.broadcast({ name: 'expedicionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackPublicidadById(index: number, item: Publicidad) {
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
    selector: 'jhi-expedicion-popup',
    template: ''
})
export class ExpedicionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private expedicionPopupService: ExpedicionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.expedicionPopupService
                    .open(ExpedicionDialogComponent as Component, params['id']);
            } else {
                this.expedicionPopupService
                    .open(ExpedicionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
