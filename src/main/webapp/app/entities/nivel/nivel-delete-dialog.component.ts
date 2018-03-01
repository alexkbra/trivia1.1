import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Nivel } from './nivel.model';
import { NivelPopupService } from './nivel-popup.service';
import { NivelService } from './nivel.service';

@Component({
    selector: 'jhi-nivel-delete-dialog',
    templateUrl: './nivel-delete-dialog.component.html'
})
export class NivelDeleteDialogComponent {

    nivel: Nivel;

    constructor(
        private nivelService: NivelService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.nivelService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'nivelListModification',
                content: 'Deleted an nivel'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-nivel-delete-popup',
    template: ''
})
export class NivelDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private nivelPopupService: NivelPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.nivelPopupService
                .open(NivelDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
