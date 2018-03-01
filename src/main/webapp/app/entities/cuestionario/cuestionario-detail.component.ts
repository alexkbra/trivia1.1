import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Cuestionario } from './cuestionario.model';
import { CuestionarioService } from './cuestionario.service';

@Component({
    selector: 'jhi-cuestionario-detail',
    templateUrl: './cuestionario-detail.component.html'
})
export class CuestionarioDetailComponent implements OnInit, OnDestroy {

    cuestionario: Cuestionario;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private cuestionarioService: CuestionarioService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCuestionarios();
    }

    load(id) {
        this.cuestionarioService.find(id).subscribe((cuestionario) => {
            this.cuestionario = cuestionario;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCuestionarios() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cuestionarioListModification',
            (response) => this.load(this.cuestionario.id)
        );
    }
}
