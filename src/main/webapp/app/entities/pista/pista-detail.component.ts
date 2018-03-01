import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Pista } from './pista.model';
import { PistaService } from './pista.service';

@Component({
    selector: 'jhi-pista-detail',
    templateUrl: './pista-detail.component.html'
})
export class PistaDetailComponent implements OnInit, OnDestroy {

    pista: Pista;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private pistaService: PistaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPistas();
    }

    load(id) {
        this.pistaService.find(id).subscribe((pista) => {
            this.pista = pista;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPistas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'pistaListModification',
            (response) => this.load(this.pista.id)
        );
    }
}
