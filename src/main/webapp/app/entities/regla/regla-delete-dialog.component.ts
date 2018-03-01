import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Regla } from './regla.model';
import { ReglaPopupService } from './regla-popup.service';
import { ReglaService } from './regla.service';

@Component({
    selector: 'jhi-regla-delete-dialog',
    templateUrl: './regla-delete-dialog.component.html'
})
export class ReglaDeleteDialogComponent {

    regla: Regla;

    constructor(
        private reglaService: ReglaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.reglaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'reglaListModification',
                content: 'Deleted an regla'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-regla-delete-popup',
    template: ''
})
export class ReglaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reglaPopupService: ReglaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.reglaPopupService
                .open(ReglaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
