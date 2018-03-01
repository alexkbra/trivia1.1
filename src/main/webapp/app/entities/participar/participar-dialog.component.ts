import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Participar } from './participar.model';
import { ParticiparPopupService } from './participar-popup.service';
import { ParticiparService } from './participar.service';

@Component({
    selector: 'jhi-participar-dialog',
    templateUrl: './participar-dialog.component.html'
})
export class ParticiparDialogComponent implements OnInit {

    participar: Participar;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private participarService: ParticiparService,
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
        if (this.participar.id !== undefined) {
            this.subscribeToSaveResponse(
                this.participarService.update(this.participar));
        } else {
            this.subscribeToSaveResponse(
                this.participarService.create(this.participar));
        }
    }

    private subscribeToSaveResponse(result: Observable<Participar>) {
        result.subscribe((res: Participar) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Participar) {
        this.eventManager.broadcast({ name: 'participarListModification', content: 'OK'});
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
    selector: 'jhi-participar-popup',
    template: ''
})
export class ParticiparPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private participarPopupService: ParticiparPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.participarPopupService
                    .open(ParticiparDialogComponent as Component, params['id']);
            } else {
                this.participarPopupService
                    .open(ParticiparDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
