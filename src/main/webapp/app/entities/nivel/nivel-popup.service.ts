import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Nivel } from './nivel.model';
import { NivelService } from './nivel.service';

@Injectable()
export class NivelPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private nivelService: NivelService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.nivelService.find(id).subscribe((nivel) => {
                    if (nivel.fechaInicio) {
                        nivel.fechaInicio = {
                            year: nivel.fechaInicio.getFullYear(),
                            month: nivel.fechaInicio.getMonth() + 1,
                            day: nivel.fechaInicio.getDate()
                        };
                    }
                    if (nivel.fechaFinal) {
                        nivel.fechaFinal = {
                            year: nivel.fechaFinal.getFullYear(),
                            month: nivel.fechaFinal.getMonth() + 1,
                            day: nivel.fechaFinal.getDate()
                        };
                    }
                    this.ngbModalRef = this.nivelModalRef(component, nivel);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.nivelModalRef(component, new Nivel());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    nivelModalRef(component: Component, nivel: Nivel): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.nivel = nivel;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
