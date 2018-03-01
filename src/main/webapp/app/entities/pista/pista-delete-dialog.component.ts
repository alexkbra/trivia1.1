import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Pista } from './pista.model';
import { PistaPopupService } from './pista-popup.service';
import { PistaService } from './pista.service';

@Component({
    selector: 'jhi-pista-delete-dialog',
    templateUrl: './pista-delete-dialog.component.html'
})
export class PistaDeleteDialogComponent {

    pista: Pista;

    constructor(
        private pistaService: PistaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pistaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'pistaListModification',
                content: 'Deleted an pista'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pista-delete-popup',
    template: ''
})
export class PistaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pistaPopupService: PistaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.pistaPopupService
                .open(PistaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
