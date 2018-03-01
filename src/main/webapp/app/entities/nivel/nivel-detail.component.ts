import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Nivel } from './nivel.model';
import { NivelService } from './nivel.service';

@Component({
    selector: 'jhi-nivel-detail',
    templateUrl: './nivel-detail.component.html'
})
export class NivelDetailComponent implements OnInit, OnDestroy {

    nivel: Nivel;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private nivelService: NivelService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInNivels();
    }

    load(id) {
        this.nivelService.find(id).subscribe((nivel) => {
            this.nivel = nivel;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInNivels() {
        this.eventSubscriber = this.eventManager.subscribe(
            'nivelListModification',
            (response) => this.load(this.nivel.id)
        );
    }
}
