import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Galerias } from './galerias.model';
import { GaleriasService } from './galerias.service';

@Component({
    selector: 'jhi-galerias-detail',
    templateUrl: './galerias-detail.component.html'
})
export class GaleriasDetailComponent implements OnInit, OnDestroy {

    galerias: Galerias;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private galeriasService: GaleriasService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGalerias();
    }

    load(id) {
        this.galeriasService.find(id).subscribe((galerias) => {
            this.galerias = galerias;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGalerias() {
        this.eventSubscriber = this.eventManager.subscribe(
            'galeriasListModification',
            (response) => this.load(this.galerias.id)
        );
    }
}
